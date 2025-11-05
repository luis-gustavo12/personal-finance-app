package com.github.Finance.dtos.requests;

import java.time.LocalDate;

public record InstallmentPurchaseRequest(
        Long categoryId,
        Long paymentMethodId,
        Long currencyId,
        String description,
        Double amount,
        Integer splits,
        LocalDate firstSplitDate,
        Integer cardId
) {
}
