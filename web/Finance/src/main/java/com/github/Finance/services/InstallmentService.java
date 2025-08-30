package com.github.Finance.services;

import com.github.Finance.dtos.installments.InstallmentsDashboardView;
import com.github.Finance.dtos.requests.InstallmentUpdateRequest;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.models.*;
import com.github.Finance.repositories.InstallmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InstallmentService {

    private final InstallmentRepository installmentRepository;
    private final AuthenticationService authenticationService;
    private final PaymentMethodsService paymentMethodsService;
    private final ExpenseService expenseService;

    public InstallmentService(InstallmentRepository installmentRepository, AuthenticationService authenticationService, PaymentMethodsService paymentMethodsService, ExpenseService expenseService) {
        this.installmentRepository = installmentRepository;
        this.authenticationService = authenticationService;
        this.paymentMethodsService = paymentMethodsService;
        this.expenseService = expenseService;
    }

    public Installment createInstallment(
            Long amount,
            int splits,
            String description,
            PaymentMethod paymentMethod,
            User user, Card card, LocalDate date, Currency currency) {

        Installment installment = new Installment();
        installment.setAmount(BigDecimal.valueOf(amount));
        installment.setSplits(splits);
        installment.setDescription(description);
        installment.setPaymentMethod(paymentMethod);
        installment.setUser(user);
        installment.setFirstSplitDate(date);
        installment.setCurrency(currency);
        if (card != null)
            installment.setCard(card);

        return installmentRepository.save(installment);

    }

    public List<Installment> getUserInstallments() {
        User user = authenticationService.getCurrentAuthenticatedUser();

        return installmentRepository.findAllByUser(user);

    }

    public Installment findInstallmentById(Long id) {
        return installmentRepository.findById(id).orElse(null);
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethodsService.getAllPaymentMethods();
    }

    public void updateInstallment(Long installmentId, InstallmentUpdateRequest request) {

        // Those are marker variables, because depending on what is updated, I need to change also
        // on expenses table
        boolean updateAmount = false, updateSplits = false, updateDescription = false;

        Installment installment = installmentRepository.findById(installmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Installment with id: " + installmentId));

        User user = authenticationService.getCurrentAuthenticatedUser();

        if (!installment.getUser().getId().equals(user.getId()))
            throw new SecurityException("You are not allowed to update this installment");

        if (request.amount() != installment.getAmount().doubleValue()) {
            installment.setAmount(BigDecimal.valueOf(request.amount()));
            updateAmount = true;
        }

        if (request.splits() !=  installment.getSplits()) {
            installment.setSplits(request.splits());
            updateSplits = true;
        }

        if (!request.description().isEmpty()) {
            if (!request.description().equals(  installment.getDescription())) {
                installment.setDescription(request.description());
                updateDescription = true;
            }
        }


        if (!request.paymentMethod().equals(installment.getPaymentMethod().getId())) {
            installment.setPaymentMethod(installment.getPaymentMethod());
        }

        Installment updatedInstallment = installmentRepository.save(installment);

        try {
            if (updateAmount || updateSplits) {

                if (updateSplits) {
                    expenseService.updateExpenseAmountByInstallmentAndSplits(
                        updatedInstallment, request.splits()
                    );
                } else
                    expenseService.updateExpenseAmountByInstallmentAndSplits(updatedInstallment,null);

            }

        } catch (Exception e) {
            log.error("Error while updating installment of id {}", installmentId);
            log.error("Message: {}", e.getMessage());
        }

    }

    public void deleteInstallment(Installment installment) {
        installmentRepository.delete(installment);
    }

    /**
     * Special method for deleting expenses that have installments.
     * Once you delete an expense that has an installment, you must delete the installment
     * on the installments table, and all referenced expenses
     * @param installmentId
     */
    public void deleteExpensesAndInstallments(Long installmentId) {

        Installment installment = installmentRepository.findById(installmentId).orElse(null);
        if (installment != null) {
            int rsl = expenseService.deleteExpenseByInstallment(installment);
            log.info("Deleting {} expense(s)", rsl);
            log.debug("Deleting installment with id: {}, description: {}", installment.getId(), installment.getDescription());
            installmentRepository.delete(installment);
        }



    }

    public InstallmentsDashboardView getInstallmentsDashboardDetails(User user) {

        List<Installment> installments = installmentRepository.findUserActiveInstallments(user);

        if (installments.isEmpty()) {
            return null;
        }

        Double totalAmount = installments.stream()
            .map(Installment::getAmount)
            .mapToDouble(BigDecimal::doubleValue)
            .sum();

        List<String> installmentsDescriptions = new ArrayList<>(installments.size());
        for (Installment installment : installments) {
            //installmentsDescriptions.add(installment.getDescription());
            log.debug("Formated: [{}]", String.format("%s (%s %.2f)", installment.getDescription(), installment.getCurrency().getCurrencyFlag(), installment.getAmount().doubleValue()));
            installmentsDescriptions.add(
                String.format("%s (%s %.2f)", installment.getDescription(), installment.getCurrency().getCurrencyFlag(), installment.getAmount().doubleValue())
            );
        }

        return new InstallmentsDashboardView(
            totalAmount,
            installments.size(),
            user.getPreferredCurrency(),
            installmentsDescriptions
        );

    }

}
