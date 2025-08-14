package com.github.Finance.dtos.response;

import com.github.Finance.models.Expense;

import java.time.LocalDate;

public record ExpenseResponse(
        Long id,
        String currency,
        String paymentMethod,
        Double amount,
        String info,
        LocalDate date,
        String category
) {

    public ExpenseResponse(Expense expense) {
        this(
                expense.getId(),
                expense.getCurrency().getCurrencyFlag(),
                expense.getPaymentMethod().getDescription(),
                expense.getAmount().doubleValue(),
                expense.getExtraInfo(),
                expense.getDate(),
                expense.getCategory().getCategoryName()
        );
    }
}
