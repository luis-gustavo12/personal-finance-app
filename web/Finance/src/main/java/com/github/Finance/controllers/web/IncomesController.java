package com.github.Finance.controllers.web;


import com.github.Finance.dtos.forms.RegisterIncomeForm;
import com.github.Finance.models.Income;
import com.github.Finance.services.IncomesService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

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


}
