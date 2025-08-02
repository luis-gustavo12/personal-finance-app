package com.github.Finance.dtos;

import com.github.Finance.models.Currency;
import com.github.Finance.models.PaymentMethod;
import com.github.Finance.models.Subscription;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SubscriptionDetailsDTO (

    Currency currency,
    double subscriptionCost,
    PaymentMethod paymentMethod,
    String status,
    LocalDateTime createdAt,
    LocalDate validSince,
    String category,
    LocalDate nextExpectedCharge,
    String subscriptionName,
    Byte dayOfCharge

)
{
}
