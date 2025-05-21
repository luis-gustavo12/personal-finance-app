package com.github.Finance.dtos.forms;

import java.math.BigDecimal;

public record AddExpenseForm(
        Long paymentMethodId,
        Long currencyId,
        BigDecimal amount,
        String extra
) {
}
