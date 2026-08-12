package com.gabriaum.picpay.admin.service;

import com.gabriaum.picpay.user.UserEntity;
import com.gabriaum.picpay.user.enums.Role;
import com.gabriaum.picpay.user.exception.UserNotFoundException;
import com.gabriaum.picpay.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> updateUserRole(
            Long userId,
            Role role
    ) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.setRole(role);
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }
}
