package com.github.Finance.dtos;

import com.github.Finance.models.Currency;
import com.github.Finance.models.PaymentMethod;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SubscriptionDetailsDTO (

    Currency currency,
    double subscriptionCost,
    PaymentMethod paymentMethod,
    String status,
    LocalDateTime createdAt,
    LocalDate validSince,
    String category

)
{
}
