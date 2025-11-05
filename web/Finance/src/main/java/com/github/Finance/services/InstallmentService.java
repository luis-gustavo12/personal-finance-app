package com.github.Finance.services;

import com.github.Finance.dtos.requests.InstallmentUpdateRequest;
import com.github.Finance.dtos.views.InstallmentView;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.models.Installment;
import com.github.Finance.models.User;
import com.github.Finance.repositories.InstallmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InstallmentService {

    private final InstallmentRepository installmentRepository;
    private final AuthenticationService authenticationService;

    public InstallmentService(InstallmentRepository installmentRepository, AuthenticationService authenticationService) {
        this.installmentRepository = installmentRepository;
        this.authenticationService = authenticationService;
    }

    /**
     * Finds an installment by its ID.
     * Throws an exception if not found.
     */
    public Installment findById(Long id) {
        return installmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Installment with id: " + id + " not found."));
    }

    /**
     * Finds an installment by ID and verifies the current user owns it.
     */
    public Installment findByIdAndVerifyOwnership(Long id) {
        User user = authenticationService.getCurrentAuthenticatedUser();
        Installment installment = findById(id);

        if (!installment.getUser().getId().equals(user.getId())) {
            log.warn("User {} attempted to access installment {} owned by user {}", user.getId(), installment.getId(), installment.getUser().getId());
            throw new SecurityException("You are not allowed to access this installment.");
        }
        return installment;
    }

    /**
     * Gets a list of installments for the current user.
     */
    public List<InstallmentView> getUserInstallments() {
        User user = authenticationService.getCurrentAuthenticatedUser();
        List<Installment> userInstallments = installmentRepository.findAllByUser(user);

        return userInstallments.stream()
                .map(InstallmentView::new)
                .collect(Collectors.toList());
    }

    /**
     * Saves changes to an installment entity.
     * This is used for both creating new and updating existing ones.
     */
    public Installment save(Installment installment) {
        return installmentRepository.save(installment);
    }

    /**
     * Deletes an installment by its ID after verifying ownership.
     */
    public void deleteById(Long installmentId) {
        // findByIdAndVerifyOwnership ensures the user is allowed to delete this
        Installment installment = findByIdAndVerifyOwnership(installmentId);
        installmentRepository.delete(installment);
        log.info("Deleted installment with id: {}", installmentId);
    }

}