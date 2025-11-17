package com.github.Finance.dtos.requests;

import java.time.LocalDate;

/**
 * Convert one simple expense to one installment
 */
public record InstallmentConversionRequest (
        Long expenseId,
        Long paymentMethodId,
        Long currencyId,
        Double amount,
        Integer splits,
        String extraInfo,
        LocalDate date,
        Long categoryId,
        Long cardId
){

}
