package com.gabriaum.picpay.user.factory;

import com.gabriaum.picpay.user.User;
import com.gabriaum.picpay.user.UserEntity;
import com.gabriaum.picpay.user.dto.RegisterDTO;

public class UserFactory {
    public static UserEntity createEntity(RegisterDTO registerDTO, String newPassword) {
        UserEntity entity = new UserEntity();
        entity.setUsername(registerDTO.username());
        entity.setEmail(registerDTO.email());
        entity.setCpf(registerDTO.cpf());
        entity.setPassword(newPassword);
        return entity;
    }

    public static User createResponse(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getCpf(),
                entity.getRole(),
                entity.getBalance()
        );
    }
}
