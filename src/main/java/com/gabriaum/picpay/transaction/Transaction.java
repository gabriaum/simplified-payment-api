package com.gabriaum.picpay.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Transaction(
        UUID id,
        Long payerId,
        Long payeeId,
        BigDecimal value,
        String description,
        LocalDateTime createdAt
) {}