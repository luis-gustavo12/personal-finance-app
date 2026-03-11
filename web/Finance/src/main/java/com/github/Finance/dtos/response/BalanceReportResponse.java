package com.github.Finance.dtos.response;

import java.util.List;

public record BalanceReportResponse (
    List<IncomesDetailResponse> incomes,
    List<ExpenseResponse> expenses
) {
}
