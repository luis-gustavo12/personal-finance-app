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
import com.github.Finance.services.CardExpenseService;
import com.github.Finance.services.CardService;
import com.github.Finance.services.CurrencyService;
import com.github.Finance.services.EncryptionService;
import com.github.Finance.services.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.Finance.models.PaymentMethod;
import com.github.Finance.services.PaymentMethodsService;

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

    public ExpensesController(PaymentMethodsService paymentMethodsService, CurrencyService currencyService, ExpenseService expenseService,
    CardExpenseService cardExpenseService, CardService cardService, EncryptionService encryptionService) {

        this.paymentMethodsService = paymentMethodsService;
        this.currencyService = currencyService;
        this.expenseService = expenseService;
        this.cardExpenseService = cardExpenseService;
        this.cardService = cardService;
        this.encryptionService = encryptionService;
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
        model.addAttribute("hasCreditCards", !expenseService.getUserCards().isEmpty());
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

        return "redirect:/expenses/details/" + response.getId();

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


     //End of detailed payment GET Routes section




    /**
     * 
     * Returns the details of the user expense
     * 
     * 
     * 
     * @param id the expense id
     *
     */
    
     
    // TODO: Implement Insecure Direct Object Reference validation
    @GetMapping("/details/{id}")
    public String getExpenseDetails(@PathVariable Long id, Model model, HttpSession session) {

        Expense expense = expenseService.findExpenseById(id);

        switch (expense.getPaymentMethod().getDescription()) {
            case "CREDIT CARD":
            case "DEBIT CARD": {
                CardExpense cardExpense = cardExpenseService.findCardExpense(id);
                if (cardExpense == null) {
                // If details of the expense id is null, so the user must fill it
                    session.setAttribute("expenseId", id);
                    return "redirect:/expenses/details/fill-expense/" + id;
                }
                model.addAttribute("cardDetail", cardExpense);
                return "credit-card-details";
            }

            default:
                break;
        }

        return "dashboard";
    }
    

    /**
     * 
     * Route for filling the detailed information about cards expenses
     * 
     */
    @GetMapping("/details/fill-expense/{id}")
    public String createCardExpenseDetails(Model model, @PathVariable Long id) {
        // Give the view the necessary information for filling that specific type of form
        List<CardView> userCards = cardService.getUserRegisteredCards();
        model.addAttribute("cardsList", userCards);
        return "fill-card-expense-details";

    }

    @PostMapping("/details/fill-expense/{id}")
    public String formCreateExpense(AddExpenseDetailsForm form, @PathVariable("id") Long expenseId) {

        cardExpenseService.processExpenseDetails(form, expenseId);



        return "redirect:/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String editIncome(@PathVariable Long id, Model model) {

        Expense expense = expenseService.findExpenseById(id);
        expenseService.validateExpenseByUser(expense);

        model.addAttribute("expense", expense);
        model.addAttribute("paymentMethods", paymentMethodsService.getAllPaymentMethods());
        model.addAttribute("currencies", currencyService.findAllCurrencies());


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
