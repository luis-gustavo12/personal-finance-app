package com.github.Finance.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateExpenseRequest(
        BigDecimal amount,
        LocalDate date,
        String extraInfo,
        Long categoryId,
        Long paymentMethodId,
        Long currencyId,
        Long cardId
) {}