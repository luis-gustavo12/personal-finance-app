package com.github.Finance.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.github.Finance.dtos.UpdateExpenseDTO;
import com.github.Finance.dtos.views.CardView;
import com.github.Finance.models.Currency;
import com.github.Finance.models.Subscription;
import org.springframework.stereotype.Service;


import com.github.Finance.dtos.forms.AddExpenseForm;
import com.github.Finance.dtos.views.ExpenseDetails;
import com.github.Finance.dtos.views.ExpenseView;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.mappers.ExpenseMapper;
import com.github.Finance.models.Expense;
import com.github.Finance.models.User;
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

    public ExpenseService(CurrencyService currencyService, PaymentMethodsService paymentMethodsService, ExpenseRepository expenseRepository, AuthenticationService authenticationService, CardService cardService, CategoryService categoryService, SubscriptionService subscriptionService) {
        this.currencyService = currencyService;
        this.paymentMethodsService = paymentMethodsService;
        this.repository = expenseRepository;
        this.authenticationService = authenticationService;
        this.cardService = cardService;
        this.categoryService = categoryService;
        this.subscriptionService = subscriptionService;
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

}
