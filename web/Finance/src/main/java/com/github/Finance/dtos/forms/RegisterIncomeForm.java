package com.github.Finance.dtos.forms;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterIncomeForm (
    @NotNull Long currencyId,
    @NotNull Double incomeAmount,
    @NotNull Long paymentMethodId,
    @NotNull LocalDate incomeDate,
    @Size(max = 50) String incomeDescription
){
}
