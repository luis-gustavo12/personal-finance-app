package com.github.Finance.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * Converts one installment expense into a normal expense
 *
 * @param amount
 * @param description
 * @param currencyId
 * @param date
 * @param categoryId
 * @param cardId
 */
public record SimpleExpenseConversionRequest(
        Double amount,
        String description,
        Long currencyId,
        LocalDate date,
        Long categoryId,
        Long cardId,
        Long paymentMethodId
) {}