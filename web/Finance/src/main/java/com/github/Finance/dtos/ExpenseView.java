package com.github.Finance.dtos;

import com.github.Finance.models.Expense;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseView {
    
    private Long id;
    private Long paymentMethodId;
    private String paymentMethodName;


}
