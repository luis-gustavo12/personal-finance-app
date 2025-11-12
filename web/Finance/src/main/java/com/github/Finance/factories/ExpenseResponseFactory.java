package com.github.Finance.factories;

import com.github.Finance.dtos.installments.InstallmentDTO;
import com.github.Finance.dtos.response.ExpenseResponse;
import com.github.Finance.dtos.views.CardView;
import com.github.Finance.models.Expense;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExpenseResponseFactory {

    private final CardViewFactory cardViewFactory;

    public ExpenseResponseFactory(CardViewFactory cardViewFactory) {
        this.cardViewFactory = cardViewFactory;
    }

    public ExpenseResponse create(Expense expense) {
        // Use the injected CardViewFactory for nested conversion
        CardView cardView = expense.getCard() != null
                ? cardViewFactory.create(expense.getCard())
                : null;

        // Create Installment DTO if available
        InstallmentDTO installmentDTO = expense.getInstallment() != null
                ? new InstallmentDTO(expense.getInstallment())
                : null;

        return new ExpenseResponse(
                expense.getId(),
                expense.getCurrency().getCurrencyFlag(),
                expense.getPaymentMethod().getDescription(),
                expense.getAmount().doubleValue(),
                expense.getExtraInfo(),
                expense.getDate(),
                expense.getCategory().getCategoryName(),
                installmentDTO,
                cardView
        );
    }

    public List<ExpenseResponse> createList(List<Expense> expenses) {
        return expenses.stream()
                .map(this::create)
                .collect(Collectors.toList());
    }
}