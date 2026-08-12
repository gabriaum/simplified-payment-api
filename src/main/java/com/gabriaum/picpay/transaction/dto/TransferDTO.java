package com.gabriaum.picpay.transaction.dto;

import java.math.BigDecimal;

public record TransferDTO(
        BigDecimal value,
        Long payeeId
) {}