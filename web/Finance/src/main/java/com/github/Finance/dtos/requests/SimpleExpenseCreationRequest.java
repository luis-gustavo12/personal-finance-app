package com.github.Finance.dtos.requests;

import java.time.LocalDate;

public record SimpleExpenseCreationRequest(
        Long paymentMethodId,
        Long currencyId,
        Double amount,
        String extraInfo,
        LocalDate date,
        Long categoryId,
        Integer cardId
) {
}
