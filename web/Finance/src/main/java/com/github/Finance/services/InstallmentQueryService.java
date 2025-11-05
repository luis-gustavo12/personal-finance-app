package com.github.Finance.services;

import com.github.Finance.dtos.installments.InstallmentsDashboardView;
import com.github.Finance.models.Installment;
import com.github.Finance.models.User;
import com.github.Finance.repositories.InstallmentRepository;
import lombok.extern.slf4j.Slf4j; // Added for logging
import org.springframework.stereotype.Service;

import java.math.BigDecimal; // Added for accurate calculations
import java.math.RoundingMode; // Added for accurate calculations
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j // Added from your original service
public class InstallmentQueryService {

    private final AuthenticationService authenticationService;
    private final InstallmentRepository installmentRepository;

    public InstallmentQueryService(AuthenticationService authenticationService, InstallmentRepository installmentRepository) {
        this.authenticationService = authenticationService;
        this.installmentRepository = installmentRepository;
    }

    public InstallmentsDashboardView getInstallmentsDashboardDetails(User user) {
        if (user == null)
            authenticationService.getCurrentAuthenticatedUser();

        List<Installment> installments = installmentRepository.findUserActiveInstallments(user);

        if (installments == null || installments.isEmpty()) {
            log.info("No active installments found for user {}", user.getId());

            return new InstallmentsDashboardView(
                    0.0,
                    0,
                    user.getPreferredCurrency(),
                    new ArrayList<>(),
                    0.0
            );
        }

        double totalAmount = 0.0;
        double sumOfPerSplitAverages = 0.0;
        List<String> installmentsDescriptions = new ArrayList<>(installments.size());

        for (Installment installment : installments) {
            BigDecimal installmentAmount = installment.getAmount();
            BigDecimal averagePerSplit = BigDecimal.ZERO;

            if (installment.getSplits() > 0) {
                averagePerSplit = installmentAmount.divide(
                        new BigDecimal(installment.getSplits()),
                        2,
                        RoundingMode.HALF_UP
                );
            }

            totalAmount += installmentAmount.doubleValue();
            sumOfPerSplitAverages += averagePerSplit.doubleValue();

            String formatted = String.format("%s (%s %.2f - %d x %s %.2f)",
                    installment.getDescription(),
                    installment.getCurrency().getCurrencyFlag(),
                    installmentAmount.doubleValue(),
                    installment.getSplits(),
                    installment.getCurrency().getCurrencySymbol(),
                    averagePerSplit.doubleValue() // Use our calculated value
            );
            log.debug("Formatted: [{}]", formatted);
            installmentsDescriptions.add(formatted);
        }

        log.info("Calculated dashboard for user {}: totalAmount={}, sumOfAverages={}",
                user.getId(), totalAmount, sumOfPerSplitAverages);

        return new InstallmentsDashboardView(
                totalAmount,
                installments.size(),
                user.getPreferredCurrency(),
                installmentsDescriptions,
                sumOfPerSplitAverages
        );
    }
}