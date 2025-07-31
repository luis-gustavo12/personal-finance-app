package com.github.Finance.controllers.web;

import java.util.List;


import com.github.Finance.dtos.UpdateExpenseDTO;
import com.github.Finance.dtos.views.*;
import com.github.Finance.dtos.forms.AddExpenseDetailsForm;
import com.github.Finance.dtos.forms.AddExpenseForm;
import com.github.Finance.models.Card;
import com.github.Finance.models.CardExpense;
import com.github.Finance.models.Currency;
import com.github.Finance.models.Expense;
import com.github.Finance.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.Finance.models.PaymentMethod;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;



/**
 * 
 * 
 * Class for most expenses part
 * 
 * NOTE: maybe, details may get bigger, so it'd be a good idea to split it
 * 
 */

@Controller
@RequestMapping("/expenses")
@Slf4j
public class ExpensesController {

    private final PaymentMethodsService paymentMethodsService;
    private final CurrencyService currencyService;
    private final ExpenseService expenseService;
    private final CardExpenseService cardExpenseService;
    private final CardService cardService;
    private final EncryptionService encryptionService;
    private final CategoryService categoryService;

    public ExpensesController(PaymentMethodsService paymentMethodsService, CurrencyService currencyService, ExpenseService expenseService,
                              CardExpenseService cardExpenseService, CardService cardService, EncryptionService encryptionService, CategoryService categoryService) {

        this.paymentMethodsService = paymentMethodsService;
        this.currencyService = currencyService;
        this.expenseService = expenseService;
        this.cardExpenseService = cardExpenseService;
        this.cardService = cardService;
        this.encryptionService = encryptionService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String expenses(Model model) {
        List<Expense> expenses = expenseService.getUserExpenses();
        List<ExpenseDetails> expenseDetails = expenseService.getExpenseDetails(expenses);
        model.addAttribute("expenses", expenseDetails);
        return "expenses";
    }

    @GetMapping("/create")
    public String displayExpense(Model model) {

        List<PaymentMethod> paymentMethods = paymentMethodsService.getAllPaymentMethods();
        List<Currency> currencies = expenseService.findAllCurrenciesByUser();
        model.addAttribute("hasCreditCards", !expenseService.getUserCards().isEmpty());
        model.addAttribute("currencies", currencies);
        model.addAttribute("paymentMethods", paymentMethods);
        model.addAttribute("categories", categoryService.getAllUserCategories());
        return "create-expense";
    }

    /**
     * 
     * After saving the expense, users must follow the next "step"
     * that consists of detailing data about that expense
     * 
     * @param form The first form
     * @return Redirect to the detail page of the related payment method id
     */
    @PostMapping("/create")
    public String createExpense(AddExpenseForm form) {

        ExpenseView response = expenseService.saveExpense(form);

        return "redirect:/expenses/details/" + response.getId();

    }


    @GetMapping("/edit/{id}")
    public String editIncome(@PathVariable Long id, Model model) {

        Expense expense = expenseService.findExpenseById(id);
        expenseService.validateExpenseByUser(expense);

        model.addAttribute("expense", expense);
        model.addAttribute("paymentMethods", paymentMethodsService.getAllPaymentMethods());
        model.addAttribute("currencies", expenseService.findAllCurrenciesByUser());


        return "edit-expense";
    }

    @PostMapping("/edit/{id}")
    public String editIncomeResponse(@PathVariable Long id, UpdateExpenseDTO expense) {

        expenseService.validateExpenseByUser(expenseService.findExpenseById(id));
        expenseService.updateExpense(expense, id);

        return "redirect:/expenses";

    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {

        expenseService.validateExpenseByUser(expenseService.findExpenseById(id));
        expenseService.deleteExpense(id);

        return "redirect:/expenses";

    }

    
}
