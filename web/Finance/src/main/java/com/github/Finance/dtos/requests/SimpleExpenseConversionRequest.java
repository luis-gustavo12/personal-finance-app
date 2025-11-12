package com.github.Finance.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * Converts one installment expense into a normal expense
 *
 * @param newAmount
 * @param newExtraInfo
 * @param currencyId
 * @param date
 * @param categoryId
 * @param cardId
 */
public record SimpleExpenseConversionRequest(
        BigDecimal newAmount,
        String newExtraInfo,
        Long currencyId,
        LocalDate date,
        Long categoryId,
        Long cardId
) {}