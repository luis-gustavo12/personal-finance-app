package com.github.Finance.dtos.requests;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;

import java.time.LocalDate;

public record IncomeCreationRequest (
        @NotNull Double amount,@NotNull Long paymentMethodId, @NotNull Long currencyId ,@NotNull LocalDate date,@NotNull String description
) {
}
