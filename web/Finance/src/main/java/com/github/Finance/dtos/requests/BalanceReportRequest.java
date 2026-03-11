package com.github.Finance.dtos.requests;

import java.time.LocalDate;

public record BalanceReportRequest (
    LocalDate minimumDate,
    LocalDate maximumDate
) {
}
