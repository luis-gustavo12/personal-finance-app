package com.github.Finance.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.Finance.dtos.UpdateExpenseDTO;
import com.github.Finance.dtos.UserSumResultDTO;
import com.github.Finance.dtos.forms.IncomeExpenseFilterForm;
import com.github.Finance.dtos.views.CardView;
import com.github.Finance.models.*;
import com.github.Finance.specifications.ExpensesSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import com.github.Finance.dtos.forms.AddExpenseForm;
import com.github.Finance.dtos.views.ExpenseDetails;
import com.github.Finance.dtos.views.ExpenseView;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.mappers.ExpenseMapper;
import com.github.Finance.repositories.ExpenseRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ExpenseService {

    private final ExpenseRepository repository;
    private final CurrencyService currencyService;
    private final PaymentMethodsService paymentMethodsService;
    private final AuthenticationService authenticationService;
    private final CardService cardService;
    private final CategoryService categoryService;
    private final SubscriptionService subscriptionService;
    private final ExchangeRateService exchangeRateService;

    public ExpenseService(CurrencyService currencyService, PaymentMethodsService paymentMethodsService, ExpenseRepository expenseRepository, AuthenticationService authenticationService, CardService cardService, CategoryService categoryService, SubscriptionService subscriptionService, ExchangeRateService exchangeRateService) {
        this.currencyService = currencyService;
        this.paymentMethodsService = paymentMethodsService;
        this.repository = expenseRepository;
        this.authenticationService = authenticationService;
        this.cardService = cardService;
        this.categoryService = categoryService;
        this.subscriptionService = subscriptionService;
        this.exchangeRateService = exchangeRateService;
    }


    public ExpenseView saveExpense(AddExpenseForm form) {

        Expense expense = new Expense();
        expense.setAmount(form.amount());
        expense.setCurrency( currencyService.findCurrency(form.currencyId()) );
        if (!form.extra().isEmpty()) 
            expense.setExtraInfo(form.extra());
        expense.setPaymentMethod(paymentMethodsService.findPaymentMethod(form.paymentMethodId()));
        expense.setUser(authenticationService.getCurrentAuthenticatedUser());
        expense.setDate(form.date());
        if (!categoryService.validCategory(form.category())) {
            throw new ResourceNotFoundException("User can't access this resource!!");
        }
        expense.setCategory(categoryService.getCategoryById(form.category()));
        expense = repository.save(expense);
        

        return ExpenseMapper.fromEntityToView(expense);
    }

    public List<Expense> getUserExpenses() {
        User user = authenticationService.getCurrentAuthenticatedUser();
        return repository.findByUser(user);
    }

    public List<Expense> getUserExpensesByTime(User user, int month, int year) {
        return repository.findExpensesByMonthAndYear(user, month, year);
    }

    /**
     * 
     * Iterates over Expense, and generate one link for each expense
     * 
     * The idea is that on the expenses.html file, an extra column gets the link for that
     * expense in specific
     * 
     * @param expenses
     * @return
     */
    public List<String> paymentFormDetailLink(List<Expense> expenses) {

        List<String> list = new ArrayList<>();

        for (Expense expense : expenses) {
            list.add(String.format("/expenses/details/%d", expense.getId()));    
        }

        return list;

    }




    public Expense findExpenseById(Long id) {
        return repository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Expense not found!!"));
    }

    public List<ExpenseDetails> getExpenseDetails (List<Expense> expenses) {

        List<ExpenseDetails> expenseDetails = new ArrayList<ExpenseDetails>(expenses.size());

        for (Expense expense : expenses) {
            ExpenseDetails obj = new ExpenseDetails();
            obj.setId(expense.getId());
            obj.setPaymentMethod(expense.getPaymentMethod());
            obj.setCurrency(expense.getCurrency());
            obj.setAmount(expense.getAmount());
            obj.setExtraInfo(expense.getExtraInfo());
            obj.setDetailedLink(String.format("/expenses/details/%d", expense.getId()));
            obj.setCategory(expense.getCategory());
            obj.setDate(expense.getDate());
            expenseDetails.add(obj);
        }

        return expenseDetails;

    }

    public List<CardView> getUserCards() {
        return cardService.getUserRegisteredCards();
    }

    public List<Expense> findExpenseByUserAndPeriod(User user, LocalDate startDate, LocalDate endDate) {
        return repository.findExpensesByUserAndPeriod(user, startDate, endDate);
    }

    public void validateExpenseByUser(Expense expense) {

        User currentUser = authenticationService.getCurrentAuthenticatedUser();

        if (!expense.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not allowed to perform this operation");
        }


    }

    public void updateExpense(UpdateExpenseDTO expenseToUpdate, Long id) {

        Expense expense = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found!!"));

        expense.setCurrency(currencyService.findCurrency(expenseToUpdate.currency()));
        expense.setAmount(expenseToUpdate.amount());
        expense.setPaymentMethod(paymentMethodsService.findPaymentMethod(expenseToUpdate.paymentMethod()));
        expense.setExtraInfo(expenseToUpdate.extraInfo());
        expense.setDate(expenseToUpdate.date());

        repository.save(expense);

        log.info("New expense updated successfully!!!");

    }

    public void deleteExpense(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Expense not found!!!");
        }

        User currentUser = authenticationService.getCurrentAuthenticatedUser();

        Expense expense = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found!!!"));

        if (!expense.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not allowed to perform this operation");
        }

        repository.deleteById(id);
        log.info("Expense with id {} deleted successfully!!!", id);


    }

    public List<Currency> findAllCurrenciesByUser() {
        User user = authenticationService.getCurrentAuthenticatedUser();
        return currencyService.findAllCurrenciesByUserAndExpense(user);
    }

    /**
     * Method responsible for creating a new expense, given an already
     * existent subscription. A subscription is a type of expense
     *
     */
    public void generateExpenseFromSubscription() {

        // First things first, query all expenses where the day of charging is today
        List<Subscription> expensesChargedForToday = subscriptionService.getAllSubscriptionsForToday();

        for (Subscription subscription : expensesChargedForToday) {

            Expense expense = new Expense();
            expense.setPaymentMethod(subscription.getPaymentMethod());
            expense.setCurrency(subscription.getCurrency());
            expense.setAmount(subscription.getCost());
            expense.setExtraInfo(String.format("Subscription [%s]", subscription.getName()));
            expense.setCategory(subscription.getCategory());
            expense.setSubscription(subscription);
            expense.setDate(LocalDate.now());
            expense.setUser(subscription.getUser());
            newExpense(expense);
            log.info("New expense created successfully!!!");
        }


    }

    public Expense newExpense(Expense expense) {
        return repository.save(expense);
    }


    public Expense saveExpense(Expense expense) {
        return repository.save(expense);
    }

    /**
     * Method responsible for getting the users most recent expenses. So far, expenses
     * set until today and from 45 days
     * @return The desired list
     */
    public List<Expense> getUserMostRecentExpenses() {
        User user = authenticationService.getCurrentAuthenticatedUser();

        return repository.findExpensesByUserAndPeriod(user, LocalDate.now().minusDays(45), LocalDate.now());

    }

    public List<Expense> getExpenseFilters(IncomeExpenseFilterForm form) {

        Specification<Expense> user = ExpensesSpecification.setUser(authenticationService.getCurrentAuthenticatedUser());
        Specification<Expense> month = ExpensesSpecification.hasMonth(form.month());
        Specification<Expense> year = ExpensesSpecification.hasYear(form.year());
        Specification<Expense> currency = ExpensesSpecification.hasCurrency(form.currencyFlag());
        Specification<Expense> paymentMethods = ExpensesSpecification.hasPaymentMethod(form.paymentMethodId());
        Specification<Expense> min = ExpensesSpecification.hasMinimum(form.minimumAmount());
        Specification<Expense> max =  ExpensesSpecification.hasMaximum(form.maximumAmount());
        Specification<Expense> category = ExpensesSpecification.hasCategory(form.categoryId());

        Specification<Expense> spec = Specification
                .where(user)
                .and(month)
                .and(year)
                .and(currency)
                .and(paymentMethods)
                .and(min)
                .and(max)
                .and(category);

        return repository.findAll(spec);

    }

    /**
     * Very identical to IncomesService.getIncomesSum method
     * @param expenses The expenses you want to have the sum of
     * @return The Sum of the expenses in the user preferred currency
     */
    public UserSumResultDTO getExpensesSum(List<Expense> expenses) {

        User user = authenticationService.getCurrentAuthenticatedUser();

        String userCurrency = user.getPreferredCurrency().getCurrencyFlag();
        BigDecimal sum = BigDecimal.ZERO;

        Map<String, List<Expense>> expensesByCurrency = expenses.stream()
                .collect(Collectors.groupingBy(expense -> expense.getCurrency().getCurrencyFlag()));

        for (Map.Entry<String, List<Expense>> entry : expensesByCurrency.entrySet()) {
            String currency = entry.getKey();
            List<Expense> currencyExpenses = entry.getValue();

            BigDecimal currencySum = currencyExpenses.stream()
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (currency.equals(userCurrency)) {
                sum = sum.add(currencySum);
                continue;
            }

            try {
                Double conversionRate = exchangeRateService.getExchangeRate(currency, userCurrency, LocalDate.now());
                BigDecimal conversion = BigDecimal.valueOf(conversionRate);
                sum = sum.add(conversion);
            } catch (Exception e) {
                log.error("Failed to calculate conversion rate {} ", e.getMessage());
            }

            log.info("Sum for Currency {}: [{}]", currency, sum);

        }

        return new UserSumResultDTO(userCurrency, sum.doubleValue());

    }

    /**
     * Method intended to update the amount and the splits on expenses, after it
     * was modified on installments table.
     * @param installment The installment that will be indexed
     * @param splits The new splits amount. If it is null, that means that no splits were changed, only final value
     */
    public void updateExpenseAmountByInstallmentAndSplits(Installment installment, Integer splits) {

        double splitAmount;
        List<Expense> expenses = repository.findExpensesByInstallment(installment);

        if (splits == null || splits.equals(expenses.size())) {
            log.debug("No splits specified, or number of splits keeps unchanged");
            splitAmount = installment.getAmount().doubleValue() / installment.getSplits();

            for (Expense expense : expenses) {
                expense.setAmount(BigDecimal.valueOf(splitAmount));
                repository.save(expense);
            }
            return;
        }

        int splitsIndex = 1;
        splitAmount = installment.getAmount().doubleValue() / splits;
        String declaration = installment.getDescription();
        // If the splits exceed the new ones, so we need to insert new expenses
        if (splits > expenses.size()) {
            Expense lastExpense = new Expense();
            for (Expense expense : expenses) {
                expense.setAmount(BigDecimal.valueOf(splitAmount));
                expense.setExtraInfo(
                    String.format("Splits [%d/%d] - %s", splitsIndex, splits, declaration)
                );
                lastExpense = repository.save(expense);
                splitsIndex++;
            }

            log.info("here");

            for (; splitsIndex < splits + 1; splitsIndex++) {
                Expense expense = new Expense();
                expense.setPaymentMethod(lastExpense.getPaymentMethod());
                expense.setAmount(BigDecimal.valueOf(splitAmount));
                expense.setExtraInfo(
                    String.format("Splits [%d/%d] - %s", splitsIndex, splits, declaration)
                );
                expense.setCurrency(lastExpense.getCurrency());
                expense.setDate(lastExpense.getDate().plusMonths(1));
                expense.setCategory(lastExpense.getCategory());
                expense.setInstallment(lastExpense.getInstallment());
                expense.setUser(lastExpense.getUser());
                lastExpense = repository.save(expense);
            }

        } else {

            // If it is less than, so it means that we need to remove the ones that will no longer be used
            for (; splitsIndex < splits + 1; splitsIndex++) {
                // For now, we're just updating the existent values
                Expense expense = expenses.get(splitsIndex - 1);
                expense.setAmount(BigDecimal.valueOf(splitAmount));
                expense.setExtraInfo(
                        String.format("Splits [%d/%d] - %s", splitsIndex, splits, declaration)
                );
                repository.save(expense);
            }
            // Now we need to remove the older ones, since the splits number are less than the previously declared
            for (; splitsIndex < expenses.size() + 1; splitsIndex++) {
                Expense expense = expenses.get(splitsIndex - 1);
                repository.delete(expense);
            }


        }

        log.info("Update expense amounts by installment");

    }

    public int deleteExpenseByInstallment(Installment installment) {
        return repository.deleteExpenseByInstallment(installment);
    }
}
