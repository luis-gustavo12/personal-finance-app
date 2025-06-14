package com.github.Finance.mappers;


import com.github.Finance.dtos.response.IncomesDetailResponse;
import com.github.Finance.models.Income;

public class IncomesMapper {

    public static IncomesDetailResponse incomesToIncome(Income income) {

        return new IncomesDetailResponse(
            income.getCurrency().getCurrencyFlag(),
            income.getAmount().doubleValue(),
            income.getPaymentMethod().getDescription(),
            income.getIncomeDate(),
            income.getDescription()
        );

    }

}
