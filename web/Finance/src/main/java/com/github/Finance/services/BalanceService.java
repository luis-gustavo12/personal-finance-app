package com.github.Finance.services;

import com.github.Finance.dtos.requests.BalanceReportRequest;
import com.github.Finance.dtos.response.BalanceReportResponse;
import com.github.Finance.dtos.response.ExpenseResponse;
import com.github.Finance.dtos.response.IncomesDetailResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceService {

    private final UserService userService;
    private final ExpenseService expenseService;
    private final IncomesService incomesService;


    public BalanceService(UserService userService, ExpenseService expenseService, IncomesService incomesService) {
        this.userService = userService;
        this.expenseService = expenseService;
        this.incomesService = incomesService;
    }

    public BalanceReportResponse getBalanceReport(BalanceReportRequest request) {

        if (request == null)
            throw new IllegalArgumentException("Request cannot be null");

        if (request.minimumDate().isAfter(request.maximumDate()))
            throw new IllegalArgumentException("Minimum date cannot be after maximum date");

        List<IncomesDetailResponse> incomes = incomesService.getIncomesByUserAndPeriod(
                null, request.minimumDate(), request.maximumDate()
        )
                .stream().map(IncomesDetailResponse::new).toList();

        List<ExpenseResponse> expenses = expenseService.getUserExpensesByPeriodOfTime(
                null, request.minimumDate(), request.maximumDate()
        ).stream().map(ExpenseResponse::new).toList();

        return new BalanceReportResponse(incomes, expenses);

    }


}
