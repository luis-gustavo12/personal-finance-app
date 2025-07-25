package com.github.Finance.controllers.web;

import com.github.Finance.dtos.forms.AddExpenseDetailsForm;
import com.github.Finance.dtos.views.CardView;
import com.github.Finance.models.CardExpense;
import com.github.Finance.models.Expense;
import com.github.Finance.services.CardExpenseService;
import com.github.Finance.services.CardService;
import com.github.Finance.services.ExpenseDetailsService;
import com.github.Finance.services.ExpenseService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/expenses/details")
public class ExpenseDetailsController {

    private final ExpenseService expenseService;
    private final CardExpenseService cardExpenseService;
    private final CardService cardService;
    private final ExpenseDetailsService expenseDetailsService;

    public ExpenseDetailsController(ExpenseService expenseService, CardExpenseService cardExpenseService, CardService cardService, ExpenseDetailsService expenseDetailsService) {
        this.expenseService = expenseService;
        this.cardExpenseService = cardExpenseService;
        this.cardService = cardService;
        this.expenseDetailsService = expenseDetailsService;
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
    public String getExpenseDetails(@PathVariable Long id, Model model, HttpSession session) {

        Expense expense = expenseService.findExpenseById(id);

        expenseDetailsService.validateUserAccess(expense.getUser().getId());

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
}
