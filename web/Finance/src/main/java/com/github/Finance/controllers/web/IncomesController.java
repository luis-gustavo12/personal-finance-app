package com.github.Finance.controllers.web;


import com.github.Finance.dtos.forms.IncomeFilterForm;
import com.github.Finance.dtos.forms.RegisterIncomeForm;
import com.github.Finance.models.Income;
import com.github.Finance.services.IncomesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/incomes")
public class IncomesController {

    private final IncomesService incomesService;


    public IncomesController(IncomesService incomesService) {
        this.incomesService = incomesService;
    }

    @GetMapping("")
    public String incomes(Model model) {
        List<Income> incomes = incomesService.getUserMonthIncomes();
        model.addAttribute("incomesData", incomesService.getUserMonthIncomes());
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
        model.addAttribute("currencies", incomesService.getCurrencies());
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
        System.out.println("IncomesController.getIncomesFilter()");
        incomesService.getIncomesDetails(form);
        return ResponseEntity.ok("Hello World");
    }



}
