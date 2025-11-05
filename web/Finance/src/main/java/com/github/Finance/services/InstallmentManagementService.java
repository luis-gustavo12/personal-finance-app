package com.github.Finance.services;

import com.github.Finance.dtos.requests.InstallmentUpdateRequest;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.models.Category;
import com.github.Finance.models.Expense;
import com.github.Finance.models.Installment;
import com.github.Finance.models.User;
import com.github.Finance.models.PaymentMethod; // Added this import
import com.github.Finance.repositories.PaymentMethodRepository; // Added this import
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * The "smart" orchestrator service.
 * It coordinates InstallmentService and ExpenseService to perform complex
 * business processes, like updating an installment and all its child expenses.
 */
@Service
@Slf4j
public class InstallmentManagementService {

    // "Dumb" services
    private final InstallmentService installmentService;
    private final ExpenseService expenseService; // Assuming you have this service

    // Other required dependencies
    private final AuthenticationService authenticationService;
    private final CategoryService categoryService;
    private final PaymentMethodRepository paymentMethodRepository; // Using repo for simple finds

    public InstallmentManagementService(InstallmentService installmentService,
                                        ExpenseService expenseService,
                                        AuthenticationService authenticationService,
                                        CategoryService categoryService,
                                        PaymentMethodRepository paymentMethodRepository) {
        this.installmentService = installmentService;
        this.expenseService = expenseService;
        this.authenticationService = authenticationService;
        this.categoryService = categoryService;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    /**
     * This is your original updateInstallment method, now living in the correct place.
     * It is @Transactional to ensure the update either fully succeeds or fails completely.
     */
    @Transactional
    public Installment updateInstallment(Long installmentId, InstallmentUpdateRequest request) {

        log.info("Updating full installment plan for id: {}", installmentId);

        boolean updateAmount = false, updateSplits = false, updateDescription = false,
                updateExpensesCategory = false, updatePaymentMethod = false;

        Installment installment = installmentService.findByIdAndVerifyOwnership(installmentId);
        User user = installment.getUser(); // We already verified the user

        Category desiredCategory = categoryService.getCategoryById(request.categoryId());
        if (desiredCategory.getUser() != null && !desiredCategory.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You are not allowed to use this category!");
        }

        Expense firstExpense = expenseService.findFirstExpenseByInstallmentId(installmentId)
                .orElseThrow(() -> new ResourceNotFoundException("No expenses found for installment " + installmentId));

        if (!desiredCategory.getId().equals(firstExpense.getCategory().getId())) {
            updateExpensesCategory = true;
        }

        if (request.amount() != installment.getAmount().doubleValue()) {
            installment.setAmount(BigDecimal.valueOf(request.amount()));
            updateAmount = true;
        }

        if (request.splits() != installment.getSplits()) {
            installment.setSplits(request.splits());
            updateSplits = true;
        }

        if (!request.description().isEmpty() && !request.description().equals(installment.getDescription())) {
            installment.setDescription(request.description());
            updateDescription = true;
        }

        if (!request.paymentMethod().equals(installment.getPaymentMethod().getId())) {
            PaymentMethod newPaymentMethod = paymentMethodRepository.findById(request.paymentMethod())
                    .orElseThrow(() -> new ResourceNotFoundException("PaymentMethod not found"));
            installment.setPaymentMethod(newPaymentMethod);
            updatePaymentMethod = true;
        }

        Installment updatedInstallment = installmentService.save(installment);


        try {

            if (updateAmount || updateSplits) {
                log.debug("Updating child expense amounts/splits for installment {}", installmentId);
                expenseService.recalculateExpenseAmountsForInstallment(updatedInstallment);
            }

            if (updateExpensesCategory) {
                log.debug("Updating child expense category for installment {}", installmentId);
                expenseService.updateFutureInstallmentCategory(installmentId, desiredCategory);
            }

            if (updatePaymentMethod) {
                log.debug("Updating child expense payment method for installment {}", installmentId);
                expenseService.updateFutureInstallmentPaymentMethod(installmentId, updatedInstallment.getPaymentMethod());
            }

            if (updateDescription) {
                log.debug("Updating child expense description for installment {}", installmentId);
                expenseService.updateInstallmentDescription(installmentId, updatedInstallment.getDescription());
            }

        } catch (Exception e) {
            log.error("Error while updating child expenses for installment id {}", installmentId, e);
            throw new RuntimeException("Failed to update child expenses, rolling back.", e);
        }

        return updatedInstallment;
    }
}