package com.github.Finance.controllers.web;

import java.util.List;


import com.github.Finance.dtos.views.*;
import com.github.Finance.dtos.forms.AddExpenseForm;
import com.github.Finance.models.Currency;
import com.github.Finance.models.Expense;
import com.github.Finance.services.CurrencyService;
import com.github.Finance.services.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.Finance.models.PaymentMethod;
import com.github.Finance.services.PaymentMethodsService;



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
public class ExpensesController {

    private final PaymentMethodsService paymentMethodsService;
    private final CurrencyService currencyService;
    private final ExpenseService expenseService;

    public ExpensesController(PaymentMethodsService paymentMethodsService, CurrencyService currencyService, ExpenseService expenseService) {
        this.paymentMethodsService = paymentMethodsService;
        this.currencyService = currencyService;
        this.expenseService = expenseService;
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
        List<Currency> currencies = currencyService.findAllCurrencies();
        model.addAttribute("currencies", currencies);
        model.addAttribute("paymentMethods", paymentMethods);
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

        return "redirect:/details/" + response.getId();
        
    }


    /**
     * 
     * detailed payment GET Routes section
     * 
     * Each route above is supposed to show to the user the details of each payment method of the user
     * 
     */
    @GetMapping("/details/credit-card")
    public String creditCardDetails() {
        return "credit-card-details";
    }
    
    @GetMapping("/details/debit-card")
    public String debitCardDetails() {
        return "redirect:/dashboard";
    }

    @GetMapping("/details/pix")
    public String pixDetails() {
        return "redirect:/dashboard";
    }

    @GetMapping("/details/cash")
    public String cashDetails() {
        return "redirect:/dashboard";
    }

    @GetMapping("/details/other")
    public String otherPaymentsDetails() {
        return "redirect:/dashboard";
    }

    /**
     * End of detailed payment GET Routes section
     */
    
    

    
    /**
     * 
     * Returns the deatils of the user expense
     * 
     * 
     * 
     * @param id the expense id
     */
    
     
    // TODO: Implement Insecure Direct Object Reference validation
    @GetMapping("/details/{id}")
    public String getExpenseDetails(@PathVariable Long id, Model model) {

        Expense expense = expenseService.findExpenseById(id);

        switch (expense.getPaymentMethod().getDescription()) {
            case "DEBIT CARD":
                return "credit-card-details";
        
            default:
                break;
        }

        return "dashboard";
    }
    
    

    
}
