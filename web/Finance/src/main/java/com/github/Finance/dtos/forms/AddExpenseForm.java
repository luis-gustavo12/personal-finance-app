package com.github.Finance.dtos.forms;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AddExpenseForm(
        Long paymentMethodId,
        Long currencyId,
        BigDecimal amount,
        String extra,
        LocalDate date,
        Long category
) {
}
