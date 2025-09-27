package com.github.Finance.dtos.views;

import java.math.BigDecimal;


public record FinancialDetailsViews (
    String currency, BigDecimal amount) {
}
