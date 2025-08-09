package com.github.Finance.services;

import com.github.Finance.models.Installment;
import com.github.Finance.models.PaymentMethod;
import com.github.Finance.models.User;
import com.github.Finance.repositories.InstallmentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InstallmentService {

    private final InstallmentRepository installmentRepository;
    private final AuthenticationService authenticationService;

    public InstallmentService(InstallmentRepository installmentRepository, AuthenticationService authenticationService) {
        this.installmentRepository = installmentRepository;
        this.authenticationService = authenticationService;
    }

    public Installment createInstallment(
        Long amount,
        int splits,
        String description,
        PaymentMethod paymentMethod,
        User user) {

        Installment installment = new Installment();
        installment.setAmount(BigDecimal.valueOf(amount));
        installment.setSplits(splits);
        installment.setDescription(description);
        installment.setPaymentMethod(paymentMethod);
        installment.setUser(user);

        return installmentRepository.save(installment);

    }

    public List<Installment> getUserInstallments() {
        User user = authenticationService.getCurrentAuthenticatedUser();

        return installmentRepository.findAllByUser(user);

    }

}
