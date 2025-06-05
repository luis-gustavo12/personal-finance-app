package com.github.Finance.dtos.views;

import com.github.Finance.models.Currency;

import java.math.BigDecimal;

/**
 *
 * DTO designed for the main subscription dashboard, which is supposed to
 * display to the user the calculation of exchange rate conversions
 *
 * @param totalAmount - The sum of all the subscriptions, given the currency symbol
 * @param currencySymbol - The desired symbol
 */

public record SubscriptionsSummaryView (
    Double totalAmount,
    String currencySymbol
)
{

}
