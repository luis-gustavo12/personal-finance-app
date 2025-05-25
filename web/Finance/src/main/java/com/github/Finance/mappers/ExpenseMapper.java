package com.github.Finance.mappers;

import com.github.Finance.dtos.views.ExpenseView;
import com.github.Finance.models.Expense;

public class ExpenseMapper {
    

    public static ExpenseView fromEntityToView(Expense entity) {

        return new ExpenseView(
            entity.getId(), entity.getPaymentMethod().getId(),
            entity.getPaymentMethod().getDescription()
        );

    }

}
