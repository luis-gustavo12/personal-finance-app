package com.github.Finance.controllers.web;

import com.github.Finance.dtos.dashboard.DashboardDTO;
import com.github.Finance.models.Expense;
import com.github.Finance.models.Income;
import com.github.Finance.models.Report;
import com.github.Finance.models.User;
import com.github.Finance.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
@Slf4j
public class DashboardController {

    private final ReportService reportService;
    private final AuthenticationService authenticationService;
    private final IncomesService incomesService;
    private final ExpenseService expenseService;

    public DashboardController(ReportService reportService, AuthenticationService authenticationService, IncomesService incomesService, ExpenseService expenseService) {
        this.reportService = reportService;
        this.authenticationService = authenticationService;
        this.incomesService = incomesService;
        this.expenseService = expenseService;
    }


    @GetMapping("")
    public String mainDashboard(Model model) {

        User user = authenticationService.getCurrentAuthenticatedUser();

        calculateIncomes(user, model);
        calculateExpenses(user, model);

        return "dashboard";
    }


    private void calculateIncomes(User user, Model model) {
        // Show to the user the incomes and expenses of the last 15 days.
        // Something interesting would be to query data that were created within 15 days, since
        // you can declare an expense of an older date
        List<Income> lastPeriodIncome = incomesService.getIncomesByUserAndPeriod(
            user,LocalDate.now().minusDays(45), LocalDate.now()
        );

        log.info("Found {} incomes for the last 45 days", lastPeriodIncome.size());
''
        List<Double> incomesAmount = lastPeriodIncome.stream()
        .map(Income::getAmount)
        .map(BigDecimal::doubleValue)
        .toList();

        Double incomesSum = incomesAmount.stream().mapToDouble(Double::doubleValue).sum();

        model.addAttribute("incomesSum", incomesSum);

    }
    
    private void calculateExpenses(User user, Model model) {

        // Same case as incomes
        List<Expense> expenses = expenseService.findExpenseByUserAndPeriod(user, LocalDate.now().minusDays(45), LocalDate.now());
        log.info("Found {} expenses for the last 45 days", expenses.size());

        List<Double> incomesAmount = expenses.stream()
            .map(Expense::getAmount)
            .map(BigDecimal::doubleValue)
            .toList();

        Double expensesSum =  incomesAmount.stream().mapToDouble(Double::doubleValue).sum();

        model.addAttribute("expensesSum", expensesSum);

    }
    
}
