package com.github.Finance.dtos.views;

public record DashboardApiView(
    FinancialDetailsViews incomesSummary,
    FinancialDetailsViews expensesSummary) {


}
