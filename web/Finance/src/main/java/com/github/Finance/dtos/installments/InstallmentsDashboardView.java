package com.github.Finance.dtos.installments;

import com.github.Finance.models.Currency;
import com.github.Finance.models.Installment;

import java.util.List;

public record InstallmentsDashboardView(
    Double totalAmount,
    Integer installments, // number of installments
    Currency currency,
    List<String> subscriptionsDetails
) {

}
