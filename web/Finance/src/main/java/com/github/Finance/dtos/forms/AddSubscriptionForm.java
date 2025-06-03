package com.github.Finance.dtos.forms;

import java.math.BigDecimal;
import java.time.LocalDate;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.NotBlank;

public record AddSubscriptionForm(
    @NotBlank String subscriptionName,
    @NotNull Long currencySelect,
    @NotNull Long subscriptionPaymentForm,
    @NotNull BigDecimal subscriptionCost,
    @NotNull LocalDate subscriptionStart,
    @NotBlank String subscriptionCategory
) 

{
    
}
