package com.github.Finance.dtos.forms;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AddExpenseDetailsForm (
    Long cardsSelect,
    Boolean installmentCheck,
    Integer splits,
    BigDecimal interestRate,
    String status,
    LocalDate transactionDate
)
    
{    

    
}
