package com.github.Finance.services;

import com.github.Finance.dtos.forms.AddExpenseForm;
import com.github.Finance.models.ExpenseDeclaration;
import com.github.Finance.models.PaymentMethod;
import com.github.Finance.models.User;
import com.github.Finance.repositories.ExpenseDeclarationRepository;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;

@Service
public class ExpenseDeclarationService {

    private final ExpenseDeclarationRepository expenseDeclarationRepository;
    private final CategoryService categoryService;
    private final AuthenticationService authenticationService;
    private final PaymentMethodsService paymentMethodsService;
    private final CurrencyService currencyService;

    public ExpenseDeclarationService(ExpenseDeclarationRepository expenseDeclarationRepository, CategoryService categoryService, AuthenticationService authenticationService, PaymentMethodsService paymentMethodsService, CurrencyService currencyService) {
        this.expenseDeclarationRepository = expenseDeclarationRepository;
        this.categoryService = categoryService;
        this.authenticationService = authenticationService;
        this.paymentMethodsService = paymentMethodsService;
        this.currencyService = currencyService;
    }

    public ExpenseDeclaration saveExpense(AddExpenseForm form) {
        ExpenseDeclaration expenseDeclaration = new ExpenseDeclaration();
        expenseDeclaration.setAmount(form.amount());
        expenseDeclaration.setInfo(form.extra());
        expenseDeclaration.setCategory(categoryService.getCategoryById(form.category()));
        User user = authenticationService.getCurrentAuthenticatedUser();
        expenseDeclaration.setUser(user);
        expenseDeclaration.setCurrency(currencyService.findCurrency(form.currencyId()));
        expenseDeclaration.setDate(form.date());
        return expenseDeclarationRepository.save(expenseDeclaration);
    }

    public ExpenseDeclaration findById(Long id) {
        return expenseDeclarationRepository.findById(id).orElse(null);
    }

    public ExpenseDeclaration setNewPaymentFormAndSave(Long paymentMethodId, ExpenseDeclaration expenseDeclaration) {
        PaymentMethod paymentMethod = paymentMethodsService.findPaymentMethod(paymentMethodId);

        if (paymentMethod == null) {
            throw new ResolutionException("Payment method not found");
        }

        expenseDeclaration.setPaymentMethod(paymentMethod);

        return expenseDeclarationRepository.save(expenseDeclaration);
    }
}
