package com.github.Finance.dtos.subscriptions;

import com.github.Finance.models.Currency;

import java.util.List;

public record SubscriptionsDashboardView(
    Integer numberOfSubscriptions,
    Currency currency,
    Double totalAmount,
    List<String> subscriptions // name of each subscription
) {
}
