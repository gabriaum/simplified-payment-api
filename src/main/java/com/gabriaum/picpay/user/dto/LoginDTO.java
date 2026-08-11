package com.gabriaum.picpay.user.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank String cpf,
        @NotBlank String password
) {}