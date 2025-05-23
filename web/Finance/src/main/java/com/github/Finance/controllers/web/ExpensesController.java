package com.github.Finance.controllers.web;

import java.util.List;


import com.github.Finance.dtos.ExpenseView;
import com.github.Finance.dtos.forms.AddExpenseForm;
import com.github.Finance.models.Currency;
import com.github.Finance.models.Expense;
import com.github.Finance.services.CurrencyService;
import com.github.Finance.services.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        model.addAttribute("expenses", expenses);
        return "expenses";
    }

    @GetMapping("/create")
    public String displayExpense(Model model) {

        List<PaymentMethod> paymentMethods = paymentMethodsService.getAllPaymentMethods();
        List<Currency> currencies = currencyService.findAllCurrencies();
        model.addAttribute("currencies", currencies);
        model.addAttribute("paymentMethods", paymentMethods);
        return "create_expense";
    }

    @PostMapping("/create")
    public String createExpense(AddExpenseForm form) {

        ExpenseView response = expenseService.saveExpense(form);

        // redirects for specific details route. Each payment method has its own
        // way of dealing with specific data.

        switch (response.getPaymentMethodName()) {
            case "CREDIT CARD":
                return "redirect:/expenses/details/credit_card";
            case "DEBIT CARD":
                return "redirect:/expenses/details/debit_card";
            case "PIX":
                return "redirect:/expenses/details/pix";
            case "CASH":
                return "redirect:/expenses/details/cash";
            default:
                return "redirect:/expenses/details/other";
        }
        
    }

    @GetMapping("/details/credit_card")
    public String creditCardDetails() {
        return "redirect:/dashboard";
    }
    
    @GetMapping("/details/debit_card")
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

    
    
    
    

    
}
