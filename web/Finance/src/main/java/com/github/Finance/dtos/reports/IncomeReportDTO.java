package com.github.Finance.dtos.reports;

import com.github.Finance.dtos.CurrencyDTO;
import com.github.Finance.dtos.UserView;
import com.github.Finance.models.Income;
import com.github.Finance.models.PaymentMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 *
 * DTO responsible for gathering incomes from different sources
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class IncomeReportDTO {
    private UserView user;
    private CurrencyDTO currency;
    private double amount;
    private PaymentMethod paymentMethod;
    private String incomeName;
    private LocalDate date;

    public IncomeReportDTO(Income income) {
        this.currency = new CurrencyDTO(income.getCurrency());
        this.amount = income.getAmount().doubleValue();
        this.paymentMethod = income.getPaymentMethod();
        this.incomeName = income.getDescription();
        this.date = income.getIncomeDate();
    }


}
