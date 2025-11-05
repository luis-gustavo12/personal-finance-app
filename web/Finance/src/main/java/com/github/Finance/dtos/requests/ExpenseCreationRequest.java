package com.github.Finance.dtos.requests;

import java.time.LocalDate;

/**
 * This is a complex DTO for creating a simple expenses. Many business rules must apply to it, but
 * it is the service who must check them
 */
public record ExpenseCreationRequest(
    Long paymentMethodId,
    Long currencyId,
    Double amount,
    String extraInfo,
    LocalDate date,
    Long categoryId,
    Boolean isSubscription,
    Integer installments,
    Integer cardId
){
}
