package com.gabriaum.picpay.user.factory;

import com.gabriaum.picpay.user.User;
import com.gabriaum.picpay.user.UserEntity;
import com.gabriaum.picpay.user.dto.RegisterDTO;

public class UserFactory {
    public static UserEntity createEntity(RegisterDTO registerDTO, String newPassword) {
        UserEntity entity = new UserEntity();
        entity.setFirstName(registerDTO.firstName());
        entity.setLastName(registerDTO.lastName());
        entity.setEmail(registerDTO.email());
        entity.setCpf(registerDTO.cpf());
        entity.setPassword(newPassword);
        if (registerDTO.role() != null && registerDTO.role().isInsertable())
            entity.setRole(registerDTO.role());

        return entity;
    }

    public static User createResponse(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getCpf(),
                entity.getRole(),
                entity.getBalance()
        );
    }
}
