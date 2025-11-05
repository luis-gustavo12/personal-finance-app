package com.github.Finance.controllers.web;

import com.github.Finance.dtos.requests.InstallmentUpdateRequest;
import com.github.Finance.models.Expense;
import com.github.Finance.models.Installment;
import com.github.Finance.repositories.InstallmentRepository;
import com.github.Finance.services.*;
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
    private final CategoryService categoryService;
    private final ExpenseService expenseService;
    private final InstallmentRepository installmentRepository;
    private final PaymentMethodsService paymentMethodsService;
    private final InstallmentManagementService installmentManagementService;

    public InstallmentsController(InstallmentService installmentService, CategoryService categoryService, ExpenseService expenseService, InstallmentRepository installmentRepository, PaymentMethodsService paymentMethodsService, InstallmentManagementService installmentManagementService) {
        this.installmentService = installmentService;
        this.categoryService = categoryService;
        this.expenseService = expenseService;
        this.installmentRepository = installmentRepository;
        this.paymentMethodsService = paymentMethodsService;
        this.installmentManagementService = installmentManagementService;
    }

    @GetMapping("")
    public String displayInstallments(Model model) {

        model.addAttribute("installments", installmentService.getUserInstallments());

        return "installments";
    }

    @GetMapping("/edit/{id}")
    public String editInstallments(@PathVariable Long id, Model model) {

        Installment installment = installmentRepository.findById(id).orElse(null);
        Expense expense = expenseService.findExpenseByInstallment(installment);
        model.addAttribute("expense", expense);
        model.addAttribute("installment", installment);
        model.addAttribute("paymentMethods", paymentMethodsService.getAllPaymentMethods());
        model.addAttribute("userCategories", categoryService.getAllUserCategories(installment.getUser()));

        return "edit-installments";
    }

    @PostMapping("/edit/{id}")
    public String postEditInstallments(@PathVariable Long id, InstallmentUpdateRequest request) {

        installmentManagementService.updateInstallment(id, request);
        return "redirect:/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deleteInstallments(@PathVariable Long id) {
        installmentRepository.deleteById(id);
        return "redirect:/dashboard";
    }

}
