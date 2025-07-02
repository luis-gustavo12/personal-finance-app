package com.github.Finance.services;

import com.github.Finance.dtos.CurrencyDTO;
import com.github.Finance.dtos.reports.ExpenseReportDTO;
import com.github.Finance.dtos.reports.IncomeReportDTO;
import com.github.Finance.dtos.reports.ReportAmountDetails;
import com.github.Finance.dtos.reports.ReportDTO;
import com.github.Finance.models.*;
import com.github.Finance.models.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Slf4j
@Service
public class MonthlyReportService {

    private final UserService userService;
    private final IncomesService incomesService;
    private final ExpenseService expenseService;
    private final ExchangeRateService exchangeRateService;
    private final ReportService reportService;

    public MonthlyReportService(UserService userService, IncomesService incomesService, ExpenseService expenseService, ExchangeRateService exchangeRateService, ReportService reportService) {
        this.userService = userService;
        this.incomesService = incomesService;
        this.expenseService = expenseService;
        this.exchangeRateService = exchangeRateService;
        this.reportService = reportService;
    }


    /**
     *
     * Main function for generating the report for each user
     * <br>
     * Every report consists of these main aspects:
     *  - Expenses
     *  - Incomes
     *
     *
     */
    public void generateMonthlyReport() {

        List<User> users = userService.getAllRegularUsers();

        log.info("Generating report for {} users", users.size());

        for (User user : users) {

            generateUserReport(user);



        }



    }

    /**
     *
     * Generate the list for the income report <br>
     * Note that the amount isn't calculated here, it is just a model for showing on the report. Other method
     * should be responsible for so
     *
     * @param user the user to be found
     * @return the IncomeReportDTO list
     */
    private List<Income> generateIncomeReport(User user) {

        // First, pick off all the user expenses of the last month, since it's generated at the beginning of the month
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        log.info("Generating income report for date {}", lastMonth);
        List<Income> incomes = incomesService.getMonthUserIncomes(lastMonth.getMonthValue(), lastMonth.getYear(), user);

        log.info("Found {} income(s) for date {}", incomes.size(), lastMonth);

        return incomes;

    }

    private List<Expense>  generateExpenseReport(User user) {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        log.info("Generating export report for date {}", lastMonth);
        List<Expense> expenses = expenseService.getUserExpensesByTime(user, lastMonth.getMonthValue(), lastMonth.getYear());

        log.info("Found {} expense(s) for date {}", expenses.size(), lastMonth);

        return expenses;
    }

    private double calculateIncomes(List<IncomeReportDTO> incomes) {

        double amount = 0;

        for (IncomeReportDTO income : incomes) {

            CurrencyDTO userCurrency = income.getUser().preferredCurrency();

            amount += exchangeRateService.getExchangeRate(income.getCurrency().currencyFlag(),
                userCurrency == null ? "BRL" : userCurrency.currencyFlag(), income.getDate()
            );

        }

        return amount;


    }

    private double calculateExpenses(List<ExpenseReportDTO> expenses) {

        double amount = 0;

        for (ExpenseReportDTO expense : expenses) {
            CurrencyDTO userCurrency = expense.user().preferredCurrency();
            amount += exchangeRateService.getExchangeRate(expense.currency().currencyFlag(),
                userCurrency == null ? "BRL" : userCurrency.currencyFlag(), expense.date()
            );
        }

        return amount;

    }


    private void generateUserReport(User user) {

        List<Income> incomes = generateIncomeReport(user);

        List<Expense> expenses = generateExpenseReport(user);

        double incomesSum = calculateIncomes(incomes.stream().map(IncomeReportDTO::new)
        .collect(Collectors.toList()));
        double expensesSum = calculateExpenses(expenses.stream().map(ExpenseReportDTO::new)
        .collect(Collectors.toList()));

        Report report = new Report();

        report.setUser(user);
        report.setCurrency(user.getPreferredCurrency());
        report.setGenerationDate(LocalDateTime.now());
        report.setExpensesSum(BigDecimal.valueOf(expensesSum));
        report.setIncomesSum(BigDecimal.valueOf(incomesSum));
    }



}
