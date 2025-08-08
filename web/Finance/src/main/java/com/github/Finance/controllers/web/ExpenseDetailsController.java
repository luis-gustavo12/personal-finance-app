package com.github.Finance.controllers.web;

import com.github.Finance.dtos.forms.AddExpenseDetailsForm;
import com.github.Finance.dtos.views.CardView;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.models.CardExpense;
import com.github.Finance.models.Expense;
import com.github.Finance.models.ExpenseDeclaration;
import com.github.Finance.models.User;
import com.github.Finance.services.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/expenses/details")
public class ExpenseDetailsController {

    private final ExpenseService expenseService;
    private final CardExpenseService cardExpenseService;
    private final CardService cardService;
    private final ExpenseDetailsService expenseDetailsService;
    private final ExpenseDeclarationService expenseDeclarationService;
    private final PaymentMethodsService paymentMethodsService;
    private final AuthenticationService authenticationService;

    public ExpenseDetailsController(ExpenseService expenseService, CardExpenseService cardExpenseService, CardService cardService, ExpenseDetailsService expenseDetailsService, ExpenseDeclarationService expenseDeclarationService, PaymentMethodsService paymentMethodsService, AuthenticationService authenticationService) {
        this.expenseService = expenseService;
        this.cardExpenseService = cardExpenseService;
        this.cardService = cardService;
        this.expenseDetailsService = expenseDetailsService;
        this.expenseDeclarationService = expenseDeclarationService;
        this.paymentMethodsService = paymentMethodsService;
        this.authenticationService = authenticationService;
    }

    /**
     *
     * detailed payment GET Routes section
     *
     * Each route above is supposed to show to the user the details of each payment method of the user
     *
     */
    @GetMapping("/credit-card")
    public String creditCardDetails() {
        return "credit-card-details";
    }

    @GetMapping("/details/debit-card")
    public String debitCardDetails() {
        return "redirect:/dashboard";
    }

    @GetMapping("/pix")
    public String pixDetails() {
        return "redirect:/dashboard";
    }

    @GetMapping("/cash")
    public String cashDetails() {
        return "redirect:/dashboard";
    }

    @GetMapping("/other")
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


    @GetMapping("/{id}")
    public String getExpenseDetails(@PathVariable Long id, Model model) {

        ExpenseDeclaration expenseDeclaration = expenseDeclarationService.findById(id);

        if (expenseDeclaration == null) {
            throw new ResourceNotFoundException("Expense declaration not found");
        }

        model.addAttribute("expenseDeclaration", expenseDeclaration);
        model.addAttribute("paymentForms", paymentMethodsService.getAllPaymentMethods());

        return "payment-method-details";
    }

    @PostMapping("/{id}")
    public String addExpenseDetails(@PathVariable Long id, Model model,
            @RequestParam("paymentForm") Long paymentMethodId) {

        return expenseDetailsService.handlePaymentMethod(id, paymentMethodId);

    }


    /**
     *
     * Route for filling the detailed information about cards expenses
     *
     */
    @GetMapping("/fill-expense/{id}")
    public String createCardExpenseDetails(Model model, @PathVariable Long id) {
        // Give the view the necessary information for filling that specific type of form
        model.addAttribute("cardsList", cardService.getUserRegisteredCards());
        return "fill-card-expense-details";

    }

    @PostMapping("/fill-expense/{id}")
    public String formCreateExpense(AddExpenseDetailsForm form, @PathVariable("id") Long expenseId) {


        cardExpenseService.processExpenseDetails(form, expenseId);


        return "redirect:/dashboard";
    }

    @GetMapping("/fill-card-expense/{id}")
    public String fillCardExpenseDetails(Model model, @PathVariable("id") Long expenseId) {
        model.addAttribute("cardsList", cardService.getUserRegisteredCards());
        return "fill-card-expense-details";
    }

    @PostMapping("/fill-card-expense/{id}")
    public String fillCardExpenseDetailsForm(AddExpenseDetailsForm form, @PathVariable("id") Long expenseId) {
        expenseDetailsService.setFinalConversion(form, expenseId);

        return "redirect:/expenses";
    }


    @GetMapping("/generate-final-expense/{id}")
    public String generateExpense(Model model, @PathVariable("id") Long expenseId) {
        return "redirect:/expenses";
    }


}
