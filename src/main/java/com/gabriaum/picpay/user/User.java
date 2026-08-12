package com.gabriaum.picpay.user;

import com.gabriaum.picpay.user.enums.Role;

import java.math.BigDecimal;

public record User(
        Long id,
        String firstName,
        String lastName,
        String email,
        String cpf,
        Role role,
        BigDecimal balance
        ) {}