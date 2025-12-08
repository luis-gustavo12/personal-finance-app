package com.github.Finance.dtos.response;

import com.github.Finance.dtos.installments.InstallmentDTO;
import com.github.Finance.dtos.views.CardView;
import com.github.Finance.models.Expense;

import java.time.LocalDate;

public record ExpenseResponse(
        Long id,
        String currency,
        String paymentMethod,
        Double amount,
        String info,
        LocalDate date,
        String category,
        InstallmentDTO installment,
        CardView cardData,
        boolean isSubscription
) {

    public ExpenseResponse(Expense expense) {
        this(
                expense.getId(),
                expense.getCurrency().getCurrencyFlag(),
                expense.getPaymentMethod().getDescription(),
                expense.getAmount().doubleValue(),
                expense.getExtraInfo(),
                expense.getDate(),
                expense.getCategory().getCategoryName(),
                expense.getInstallment() == null ? null : new InstallmentDTO(expense.getInstallment()),
                null,
             expense.getSubscription() != null
        );
    }
}
