package com.github.Finance.controllers.web;


import com.github.Finance.dtos.forms.IncomeFilterForm;
import com.github.Finance.dtos.forms.RegisterIncomeForm;
import com.github.Finance.dtos.response.IncomesDetailResponse;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.models.Income;
import com.github.Finance.models.User;
import com.github.Finance.services.AuthenticationService;
import com.github.Finance.services.IncomesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/incomes")
@Slf4j
public class IncomesController {

    private final IncomesService incomesService;
    private final AuthenticationService authenticationService;


    public IncomesController(IncomesService incomesService, AuthenticationService authenticationService) {
        this.incomesService = incomesService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("")
    public String incomes(Model model) {
        List<Income> incomes = incomesService.getCurrentMonthUserIncomes();
        model.addAttribute("incomesData", incomesService.getCurrentMonthUserIncomes());
        model.addAttribute("month", LocalDate.now().getMonthValue());
        model.addAttribute("sum", incomesService.getIncomesSum(incomes));

        // Using these streams with distinct() to make easier to generate thymeleaf
        // <option> tags, so that the only options available to the user, are options that they chose,
        // not making it possible to query an option that "doesn't exist"

        model.addAttribute("years", incomes.stream()
            .map(year -> year.getIncomeDate().getYear())
            .distinct()
            .collect(Collectors.toList()));
        model.addAttribute("currencies", incomes.stream()
            .map(income -> income.getCurrency().getCurrencyFlag())
            .distinct()
            .collect(Collectors.toList()) );
        model.addAttribute("paymentMethods", incomes.stream()
            .map(Income::getPaymentMethod)
            .distinct()
            .collect(Collectors.toList())
        );
        return "incomes";
    }

    @GetMapping("/create")
    public String getIncomePage(Model model) {
        model.addAttribute("paymentMethods", incomesService.getPaymentMethods());
        model.addAttribute("currencies", incomesService.getAllUserCurrenciesByIncome());
        return "create-incomes";
    }

    @PostMapping("/create")
    public String registerIncome(@Valid RegisterIncomeForm form) {
        incomesService.createIncome(form);
        return "redirect:/incomes";
    }


    // Ajax Requests

    @GetMapping("/filter")
    public ResponseEntity<?> getIncomesFilter(IncomeFilterForm form) {
        List<IncomesDetailResponse> responses = incomesService.getIncomesDetails(form);
        return ResponseEntity.ok(responses);
    }

    /**
     * Method that returns the form that lists all the user expenses, and allow them to edit it
     * @return the incomes form html
     */
    @GetMapping("/edit")
    public String incomeEditView(Model model) {

        // List all the incomes for views
        model.addAttribute("incomes", incomesService.getCurrentMonthUserIncomes());
        return "edit-incomes";
    }

    /**
     * Returns the form for editing the view
     * @param id Id form
     */
    @GetMapping("/edit/{id}")
    public String incomeEditForm(Model model, @PathVariable Long id) {
        Income income = incomesService.findIncomeByIdForAuthenticatedUser(id);
        model.addAttribute("income", income);
        model.addAttribute("paymentMethods", incomesService.getPaymentMethods());
        model.addAttribute("currencies", incomesService.getAllUserCurrenciesByIncome());
        return "edit-income-form";
    }

    @PostMapping("/edit/{id}")
    public String incomeEdit(@PathVariable Long id,RegisterIncomeForm form) {

        incomesService.updateExistentIncome(id, form);


        return "redirect:/incomes";
    }

    @GetMapping("/delete/{id}")
    public String deleteIncome(@PathVariable Long id) {

        incomesService.deleteIncomeById(id);

        return "redirect:/incomes";
    }

}
