package com.gabriaum.picpay.user.dto;

public record AuthenticatedDTO(
        String cpf,
        String email,
        String token
) {}