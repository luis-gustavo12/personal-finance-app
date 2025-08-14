package com.github.Finance.dtos.forms;

public record IncomeExpenseFilterForm(
    Integer month,
    Integer year,
    String currencyFlag,
    Long paymentMethodId,
    Double minimumAmount,
    Double maximumAmount,
    String description

) {
}
