package com.gabriaum.picpay.admin.dto;

import java.math.BigDecimal;

public record UpdateUserBalanceDTO(
        BigDecimal value
) {}