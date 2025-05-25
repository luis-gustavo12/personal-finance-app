package com.github.Finance.dtos.views;



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
