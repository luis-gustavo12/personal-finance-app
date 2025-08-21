package com.github.Finance.controllers.web;

import com.github.Finance.dtos.requests.InstallmentUpdateRequest;
import com.github.Finance.models.Installment;
import com.github.Finance.services.InstallmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/edit/{id}")
    public String editInstallments(@PathVariable Long id, Model model) {

        Installment installment = installmentService.findInstallmentById(id);
        model.addAttribute("installment", installment);
        model.addAttribute("paymentMethods", installmentService.getPaymentMethods());

        return "edit-installments";
    }

    @PostMapping("/edit/{id}")
    public String postEditInstallments(@PathVariable Long id, InstallmentUpdateRequest request) {

        installmentService.updateInstallment(id, request);
        return "redirect:/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deleteInstallments(@PathVariable Long id) {
        installmentService.deleteExpensesAndInstallments(id);
        return "redirect:/dashboard";
    }

}
