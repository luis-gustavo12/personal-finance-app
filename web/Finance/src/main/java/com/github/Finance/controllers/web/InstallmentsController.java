package com.github.Finance.controllers.web;

import com.github.Finance.services.InstallmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/installments")
public class InstallmentsController {

    private final InstallmentService installmentService;

    public InstallmentsController(InstallmentService installmentService) {
        this.installmentService = installmentService;
    }

    @GetMapping("")
    public String displayInstallments(Model model) {

        model.addAttribute("installments", installmentService.getUserInstallments());

        return "installments";
    }

}
