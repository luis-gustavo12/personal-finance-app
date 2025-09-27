package com.github.Finance.dtos.response;

import com.github.Finance.models.Income;

import java.time.LocalDate;

public record IncomesDetailResponse (

    String currency,
    Double amount,
    String paymentForm,
    LocalDate date,
    String extraInfo,
    Long id

) {

    public IncomesDetailResponse (Income income) {
        this(
            income.getCurrency().getCurrencyFlag(),
            income.getAmount().doubleValue(),
            income.getPaymentMethod().getDescription(),
            income.getIncomeDate(),
            income.getDescription(),
            income.getId()
        );
    }


}
