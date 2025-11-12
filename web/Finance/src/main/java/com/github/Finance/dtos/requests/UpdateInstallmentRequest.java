package com.github.Finance.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateInstallmentRequest(
        String description,
        Long categoryId,
        Long currencyId,
        Long cardId,
        BigDecimal amount,
        Integer splits,
        LocalDate date
) {}
