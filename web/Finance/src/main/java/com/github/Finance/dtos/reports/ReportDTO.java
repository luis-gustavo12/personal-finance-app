package com.github.Finance.dtos.reports;

import com.github.Finance.dtos.CurrencyDTO;
import com.github.Finance.models.Expense;
import com.github.Finance.models.User;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class ReportDTO {

    private User user;
    private List<IncomeReportDTO> incomes;
    private List<ExpenseReportDTO> expenses;
    private LocalDateTime generationDate;
    private CurrencyDTO currency;
    private ReportAmountDetails details;


}
