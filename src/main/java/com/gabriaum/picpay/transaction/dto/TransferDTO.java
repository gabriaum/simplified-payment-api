package com.gabriaum.picpay.transaction.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferDTO(
        @NotNull BigDecimal value,
        @NotNull Long payeeId,
        String description
) {}