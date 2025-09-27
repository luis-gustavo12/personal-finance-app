package com.github.Finance.services;

import com.github.Finance.dtos.response.IncomesDetailResponse;
import com.github.Finance.dtos.views.DashboardApiView;
import com.github.Finance.dtos.views.FinancialDetailsViews;
import com.github.Finance.models.Expense;
import com.github.Finance.models.Income;
import com.github.Finance.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {


    private final AuthenticationService authenticationService;
    private final IncomesService incomesService;
    private final ExpenseService expenseService;


    public DashboardService(AuthenticationService authenticationService, IncomesService incomesService, ExpenseService expenseService) {
        this.authenticationService = authenticationService;
        this.incomesService = incomesService;
        this.expenseService = expenseService;
    }


    public DashboardApiView getDashboardData() {

        User user = authenticationService.getCurrentAuthenticatedUser();
        List<Income> incomes = incomesService.getIncomesByUserAndPeriod(
            user, LocalDate.now().minusDays(35), LocalDate.now()
        );

        BigDecimal incomesSum = incomes.stream().map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Expense> expenses = expenseService.findExpenseByUserAndPeriod(
            user, LocalDate.now().minusDays(35), LocalDate.now()
        );

        BigDecimal expensesSum = expenses.stream().map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        return new DashboardApiView(
            new FinancialDetailsViews(user.getPreferredCurrency().getCurrencyFlag(), incomesSum),
            new FinancialDetailsViews(user.getPreferredCurrency().getCurrencyFlag(), expensesSum)
        );

    }

}
