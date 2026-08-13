package com.gabriaum.picpay.user.dto;

import com.gabriaum.picpay.user.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String email,
        @NotBlank String cpf,
        @NotBlank @Size(min = 8, max = 100, message = "A senha deve ter no mínimo 8 caracteres") String password,
        Role role
) {}