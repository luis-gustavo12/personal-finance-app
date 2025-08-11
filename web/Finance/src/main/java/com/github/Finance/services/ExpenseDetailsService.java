package com.github.Finance.services;

import com.github.Finance.dtos.forms.AddExpenseDetailsForm;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.models.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ExpenseDetailsService {

    private final AuthenticationService authenticationService;
    private final ExpenseDeclarationService expenseDeclarationService;
    private final CardService cardService;
    private final ExpenseService expenseService;
    private final CardExpenseService cardExpenseService;
    private final InstallmentService installmentService;

    public ExpenseDetailsService(AuthenticationService authenticationService, ExpenseDeclarationService expenseDeclarationService, CardService cardService, ExpenseService expenseService, CardExpenseService cardExpenseService, InstallmentService installmentService) {
        this.authenticationService = authenticationService;
        this.expenseDeclarationService = expenseDeclarationService;
        this.cardService = cardService;
        this.expenseService = expenseService;
        this.cardExpenseService = cardExpenseService;
        this.installmentService = installmentService;
    }

    public void validateUserAccess(Long id) {

        User user = authenticationService.getCurrentAuthenticatedUser();

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (!id.equals(user.getId())) {
            throw new SecurityException("User not authorized to perform this action");
        }


    }

    public String handlePaymentMethod(Long id, Long paymentMethodId) {

        ExpenseDeclaration expenseDeclaration = expenseDeclarationService.findById(id);

        if (expenseDeclaration == null) {
            throw new ResourceNotFoundException("Expense declaration not found");
        }

        User user = authenticationService.getCurrentAuthenticatedUser();

        if (!expenseDeclaration.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You are not allowed to add expense details");
        }

        expenseDeclaration = expenseDeclarationService.setNewPaymentFormAndSave(paymentMethodId, expenseDeclaration);

        if (expenseDeclaration.getPaymentMethod().getDescription().equals("CARD")) {
            return "redirect:/expenses/details/fill-card-expense/" + expenseDeclaration.getId();
        }


        generateExpenseForRegularPaymentsForm(expenseDeclaration);

        return "redirect:/expenses";


    }

    private void generateExpenseForRegularPaymentsForm(ExpenseDeclaration expenseDeclaration) {
        Expense expense = new Expense();
        expense.setPaymentMethod(expenseDeclaration.getPaymentMethod());
        expense.setCurrency(expenseDeclaration.getCurrency());
        expense.setAmount(expenseDeclaration.getAmount());
        expense.setExtraInfo(expenseDeclaration.getInfo());
        expense.setUser(expenseDeclaration.getUser());
        expense.setCategory(expenseDeclaration.getCategory());
        expense.setDate(expenseDeclaration.getDate());
        expenseService.saveExpense(expense);

    }

    /**
     * Here's the final step, where we get the form, turn an expense declaration into an actual expense
     * @param form
     * @param expenseId
     */
    public void setFinalConversion(AddExpenseDetailsForm form, Long expenseId) {

        Card card = cardService.getCardById(form.cardsSelect());
        User user = authenticationService.getCurrentAuthenticatedUser();

        if (card == null) {
            throw new ResourceNotFoundException("Card not found");
        }

        if (!card.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You are not allowed to add expense details");
        }

        ExpenseDeclaration expenseDeclaration = expenseDeclarationService.findById(expenseId);

        if (expenseDeclaration == null) {
            throw new ResourceNotFoundException("Expense declaration not found");
        }

        if (form.installmentCheck() && form.splits() > 1) {
            // Follow the splits check
            creditCardExpenseFlow(card, expenseDeclaration, form.splits(), form.status());
            return;
        }

        Expense expense = new Expense();
        expense.setPaymentMethod(expenseDeclaration.getPaymentMethod());
        expense.setCurrency(expenseDeclaration.getCurrency());
        expense.setAmount(expenseDeclaration.getAmount());
        expense.setExtraInfo(expenseDeclaration.getInfo());
        expense.setUser(expenseDeclaration.getUser());
        expense.setCategory(expenseDeclaration.getCategory());
        expense.setDate(expenseDeclaration.getDate());

        expenseService.saveExpense(expense);

    }


    /**
     * For each credit card declaration, one expense must be set for each split
     */
    private void creditCardExpenseFlow(Card card, ExpenseDeclaration expenseDeclaration, Integer splits, String status) {



        // Before saving the expense, create an installment record
        Installment installment = installmentService.createInstallment(
            expenseDeclaration.getAmount().longValue(),
            splits,
            expenseDeclaration.getInfo(),
            expenseDeclaration.getPaymentMethod(),
            expenseDeclaration.getUser()
        );

        double splitValue = expenseDeclaration.getAmount().doubleValue() / splits;
        LocalDate date = expenseDeclaration.getDate();

        for (int i = 0; i < splits; i++, date = date.plusMonths(1)) {

            Expense expense = new Expense();
            expense.setPaymentMethod(expenseDeclaration.getPaymentMethod());
            expense.setCurrency(expenseDeclaration.getCurrency());
            expense.setAmount(BigDecimal.valueOf(splitValue));
            String dateText = String.format("Splits [%d/%d] - %s", (i + 1), splits, expenseDeclaration.getInfo());
            expense.setExtraInfo(dateText);
            expense.setDate(date);
            expense.setUser(expenseDeclaration.getUser());
            expense.setCategory(expenseDeclaration.getCategory());
            expense.setInstallment(installment);
            expense = expenseService.saveExpense(expense);

            cardExpenseService.processExpenseDetails(expense, card, true, splits, status);

        }

    }


}
