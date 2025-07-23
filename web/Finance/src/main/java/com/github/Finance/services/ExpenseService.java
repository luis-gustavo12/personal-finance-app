package com.github.Finance.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.github.Finance.dtos.UpdateExpenseDTO;
import com.github.Finance.dtos.views.CardView;
import com.github.Finance.models.Card;
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

    public ExpenseService(CurrencyService currencyService, PaymentMethodsService paymentMethodsService, ExpenseRepository expenseRepository, AuthenticationService authenticationService, CardService cardService) {
        this.currencyService = currencyService;
        this.paymentMethodsService = paymentMethodsService;
        this.repository = expenseRepository;
        this.authenticationService = authenticationService;
        this.cardService = cardService;
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
        expense = repository.save(expense);
        

        return ExpenseMapper.fromEntityToView(expense);
    }

    public List<Expense> getUserExpenses() {
        User user = authenticationService.getCurrentAuthenticatedUser();
        List<Expense> list = repository.findByUser(user);
        return list;
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
}
