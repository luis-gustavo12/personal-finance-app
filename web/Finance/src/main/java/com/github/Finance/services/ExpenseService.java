package com.github.Finance.services;

import org.springframework.stereotype.Service;

import com.github.Finance.dtos.ExpenseView;
import com.github.Finance.dtos.forms.AddExpenseForm;
import com.github.Finance.mappers.ExpenseMapper;
import com.github.Finance.models.Expense;
import com.github.Finance.repositories.ExpenseRepository;


@Service
public class ExpenseService {

    private final ExpenseRepository repository;
    private final CurrencyService currencyService;
    private final PaymentMethodsService paymentMethodsService;

    public ExpenseService(CurrencyService currencyService, PaymentMethodsService paymentMethodsService, ExpenseRepository expenseRepository) {
        this.currencyService = currencyService;
        this.paymentMethodsService = paymentMethodsService;
        this.repository = expenseRepository;        
    }


    public ExpenseView saveExpense(AddExpenseForm form) {

        Expense expense = new Expense();
        expense.setAmount(form.amount());
        expense.setCurrency( currencyService.findCurrency(form.currencyId()) );
        if (!form.extra().isEmpty()) 
            expense.setExtraInfo(form.extra());
        expense.setPaymentMethod(paymentMethodsService.findPaymentMethod(form.paymentMethodId()));
        expense = repository.save(expense);
        

        return ExpenseMapper.fromEntityToView(expense);
    }


}
