package com.gabriaum.picpay.admin.dto;

import com.gabriaum.picpay.user.enums.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleDTO(
        @NotNull Role role
        ) {}