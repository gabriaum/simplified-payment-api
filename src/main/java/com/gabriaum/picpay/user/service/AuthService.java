package com.gabriaum.picpay.user.service;

import com.gabriaum.picpay.infra.security.generator.TokenGenerator;
import com.gabriaum.picpay.infra.security.service.CryptographyService;
import com.gabriaum.picpay.user.UserEntity;
import com.gabriaum.picpay.user.dto.AuthenticatedDTO;
import com.gabriaum.picpay.user.dto.LoginDTO;
import com.gabriaum.picpay.user.dto.RegisterDTO;
import com.gabriaum.picpay.user.exception.UserAlreadyExistsException;
import com.gabriaum.picpay.user.exception.UserNotFoundException;
import com.gabriaum.picpay.user.factory.UserFactory;
import com.gabriaum.picpay.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final CryptographyService cryptographyService;
    private final TokenGenerator tokenGenerator;

    public ResponseEntity<?> authenticate(LoginDTO loginDTO) {
        Optional<UserEntity> optionalUser = userRepository.findByCpf(loginDTO.cpf());
        if (optionalUser.isEmpty())
            throw new UserNotFoundException();

        UserEntity user = optionalUser.get();
        if (!cryptographyService.check(loginDTO.password(), user.getPassword()))
            throw new UserNotFoundException();

        AuthenticatedDTO authenticatedDTO = new AuthenticatedDTO(
                user.getCpf(),
                user.getEmail(),
                tokenGenerator.generateToken(user.getEmail())
        );

        return ResponseEntity.ok(authenticatedDTO);
    }

    @Transactional
    public ResponseEntity<?> register(RegisterDTO registerDTO) {
        if (userRepository.existsByCpfOrEmail(registerDTO.cpf(), registerDTO.email()))
            throw new UserAlreadyExistsException();

        UserEntity entity = UserFactory.createEntity(registerDTO, cryptographyService.encrypt(registerDTO.password()));
        userRepository.save(entity);

        AuthenticatedDTO authenticatedDTO = new AuthenticatedDTO(
                entity.getCpf(),
                entity.getEmail(),
                tokenGenerator.generateToken(entity.getEmail())
        );

        return ResponseEntity.ok(authenticatedDTO);
    }
}