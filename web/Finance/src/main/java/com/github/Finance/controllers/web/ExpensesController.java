package com.github.Finance.controllers.web;

import java.util.List;
import java.util.stream.Collectors;


import com.github.Finance.dtos.UpdateExpenseDTO;
import com.github.Finance.dtos.forms.IncomeExpenseFilterForm;
import com.github.Finance.dtos.response.ExpenseResponse;
import com.github.Finance.dtos.views.*;
import com.github.Finance.dtos.forms.AddExpenseForm;
import com.github.Finance.models.*;
import com.github.Finance.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    private final ExpenseService expenseService;
    private final CategoryService categoryService;
    private final ExpenseDeclarationService expenseDeclarationService;

    public ExpensesController(PaymentMethodsService paymentMethodsService, ExpenseService expenseService,
        CategoryService categoryService, ExpenseDeclarationService expenseDeclarationService) {

        this.paymentMethodsService = paymentMethodsService;
        this.expenseService = expenseService;
        this.categoryService = categoryService;
        this.expenseDeclarationService = expenseDeclarationService;
    }

    @GetMapping("")
    public String expenses(Model model) {
        List<Expense> expenses = expenseService.getUserMostRecentExpenses();
        List<ExpenseDetails> expenseDetails = expenseService.getExpenseDetails(expenses);
        model.addAttribute("expenses", expenseDetails);
        model.addAttribute("years", expenses.stream()
                .map(year -> year.getDate().getYear())
                .distinct()
                .collect(Collectors.toList()));
        model.addAttribute("currencies", expenses.stream()
                .map(expense -> expense.getCurrency().getCurrencyFlag())
                .distinct()
                .collect(Collectors.toList()));
        model.addAttribute("paymentMethods", expenses.stream()
                .map(Expense::getPaymentMethod)
                .distinct().collect(Collectors.toList()));
        model.addAttribute("categories", categoryService.getAllUserCategories());
        model.addAttribute("sum", expenseService.getExpensesSum(expenses));
        return "expenses";
    }

    @GetMapping("/create")
    public String displayExpense(Model model) {

        List<PaymentMethod> paymentMethods = paymentMethodsService.getAllPaymentMethods();
        List<Currency> currencies = expenseService.findAllCurrenciesByUser();
        model.addAttribute("hasCreditCards", !expenseService.getUserCards().isEmpty());
        model.addAttribute("currencies", currencies);
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

        ExpenseDeclaration expenseDeclaration = expenseDeclarationService.saveExpense(form);

        return "redirect:/expenses/details/" + expenseDeclaration.getId();

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

    @GetMapping("/filter")
    public ResponseEntity<?> filterExpenses(IncomeExpenseFilterForm form) {

        List<Expense> expenses = expenseService.getExpenseFilters(form);
        List<ExpenseResponse> response = expenses.stream()
                .map(ExpenseResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    
}
