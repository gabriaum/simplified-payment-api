package com.gabriaum.picpay.user.service;

import com.gabriaum.picpay.infra.security.generator.TokenGenerator;
import com.gabriaum.picpay.infra.security.service.CryptographyService;
import com.gabriaum.picpay.user.UserEntity;
import com.gabriaum.picpay.user.dto.AuthenticatedDTO;
import com.gabriaum.picpay.user.dto.LoginDTO;
import com.gabriaum.picpay.user.dto.RegisterDTO;
import com.gabriaum.picpay.user.exception.UserAlreadyExistsException;
import com.gabriaum.picpay.user.exception.UserNotFoundException;
import com.gabriaum.picpay.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CryptographyService cryptographyService;

    @Mock
    private TokenGenerator tokenGenerator;

    @InjectMocks
    private AuthService authService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setCpf("12345678900");
        user.setPassword("hashed");
        user.setBalance(BigDecimal.ZERO);
    }

    @Test
    void authenticateSuccess() {
        LoginDTO loginDTO = new LoginDTO("12345678900", "password");

        when(userRepository.findByCpf(loginDTO.cpf())).thenReturn(Optional.of(user));
        when(cryptographyService.check(loginDTO.password(), user.getPassword())).thenReturn(true);
        when(tokenGenerator.generateToken(user.getEmail())).thenReturn("token-abc");

        ResponseEntity<?> resp = authService.authenticate(loginDTO);

        assertEquals(200, resp.getStatusCode().value());
        assertInstanceOf(AuthenticatedDTO.class, resp.getBody());
        AuthenticatedDTO body = (AuthenticatedDTO) resp.getBody();
        assertEquals("token-abc", body.token());
        assertEquals(user.getCpf(), body.cpf());
    }

    @Test
    void authenticateNotFoundThrows() {
        LoginDTO loginDTO = new LoginDTO("00000000000", "password");

        when(userRepository.findByCpf(loginDTO.cpf())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.authenticate(loginDTO));
    }

    @Test
    void registerSuccess() {
        RegisterDTO registerDTO = new RegisterDTO("Jane", "Doe", "jane@example.com", "11122233344", "password");

        when(userRepository.existsByCpfOrEmail(registerDTO.cpf(), registerDTO.email())).thenReturn(false);
        when(cryptographyService.encrypt(registerDTO.password())).thenReturn("hashed-pass");
        when(tokenGenerator.generateToken(registerDTO.email())).thenReturn("token-123");

        ResponseEntity<?> resp = authService.register(registerDTO);

        assertEquals(200, resp.getStatusCode().value());
        assertInstanceOf(AuthenticatedDTO.class, resp.getBody());
        AuthenticatedDTO body = (AuthenticatedDTO) resp.getBody();
        assertEquals("token-123", body.token());
        assertEquals(registerDTO.cpf(), body.cpf());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void registerAlreadyExistsThrows() {
        RegisterDTO registerDTO = new RegisterDTO("Jane", "Doe", "jane@example.com", "11122233344", "password");

        when(userRepository.existsByCpfOrEmail(registerDTO.cpf(), registerDTO.email())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(registerDTO));
        verify(userRepository, never()).save(any());
    }
}


