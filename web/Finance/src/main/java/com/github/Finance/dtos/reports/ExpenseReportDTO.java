package com.github.Finance.dtos.reports;

import com.github.Finance.dtos.CurrencyDTO;
import com.github.Finance.dtos.UserView;
import com.github.Finance.models.Expense;
import com.github.Finance.models.PaymentMethod;
import com.github.Finance.models.User;

import java.time.LocalDate;
import java.util.List;

public record ExpenseReportDTO(
    UserView user,
    PaymentMethod paymentMethod,
    CurrencyDTO currency,
    double amount,
    String info,
    LocalDate date
) {

    public ExpenseReportDTO(Expense expense) {
        this(new UserView(expense.getUser()),
            expense.getPaymentMethod(),
            new CurrencyDTO( expense.getCurrency()),
            expense.getAmount().doubleValue(),
            expense.getExtraInfo(),
            expense.getDate()
        );
    }

}
