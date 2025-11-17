package com.github.Finance.services;

import com.github.Finance.dtos.requests.InstallmentConversionRequest;
import com.github.Finance.dtos.requests.InstallmentPurchaseRequest;
import com.github.Finance.dtos.requests.UpdateInstallmentRequest;
import com.github.Finance.dtos.views.InstallmentView;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.models.*;
import com.github.Finance.repositories.ExpenseRepository;
import com.github.Finance.repositories.InstallmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InstallmentService {

    private final InstallmentRepository installmentRepository;
    private final AuthenticationService authenticationService;
    private final ExpenseService expenseService;
    private final CategoryService categoryService;
    private final PaymentMethodsService paymentMethodsService;
    private final ExpenseRepository expenseRepository;
    private final CurrencyService currencyService;
    private final CardService cardService;

    public InstallmentService(InstallmentRepository installmentRepository, AuthenticationService authenticationService, ExpenseService expenseService, CategoryService categoryService, PaymentMethodsService paymentMethodsService, ExpenseRepository expenseRepository, CurrencyService currencyService, CardService cardService) {
        this.installmentRepository = installmentRepository;
        this.authenticationService = authenticationService;
        this.expenseService = expenseService;
        this.categoryService = categoryService;
        this.paymentMethodsService = paymentMethodsService;
        this.expenseRepository = expenseRepository;
        this.currencyService = currencyService;
        this.cardService = cardService;
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

    @Transactional
    public List<Expense> updateInstallmentSeries(Long expenseId, UpdateInstallmentRequest request) {

        Installment installment = installmentRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Installment not found"));

        validateInstallmentByUser(installment);

        boolean recalculate = false;

        if (request.amount() != null) {
            if (!installment.getAmount().equals(request.amount())) recalculate = true;
            installment.setAmount(request.amount());
        }
        if (request.splits() != null) {
            if (installment.getSplits() != request.splits()) recalculate = true;
            installment.setSplits(request.splits());
        }
        if (request.description() != null) {
            if (!installment.getDescription().equals(request.description())) recalculate = true;
            installment.setDescription(request.description());
        }
        if (request.currencyId() != null) installment.setCurrency(currencyService.findCurrency(
                request.currencyId()
        ));

        if (request.date() != null) {
            if (!request.date().equals(installment.getFirstSplitDate())) recalculate = true;
            installment.setFirstSplitDate(request.date());
        }

        installmentRepository.save(installment);


        if (recalculate) {
            expenseService.recalculateExpenseAmountsForInstallment(
                    installment, request
            );
        }



        return expenseService.findExpensesByInstallment(installment);
    }

    @Transactional
    public void deleteInstallmentSeries(Long installmentId) {
        Installment installment = installmentRepository.findById(installmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Installment not found"));

        validateInstallmentByUser(installment);

        int deletedExpenses = expenseService.deleteExpenseByInstallment(installment);
        log.info("Deleted {} expenses for installment {}", deletedExpenses, installmentId);

        // 2. Delete the parent Installment record
        installmentRepository.delete(installment);
        log.info("Deleted installment {} successfully", installmentId);
    }

    private void validateInstallmentByUser(Installment installment) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        if (!installment.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not allowed to perform this operation on this resource.");
        }
    }

    @Transactional
    public List<Expense> createNewInstallmentExpense(InstallmentPurchaseRequest request) {

        Category category = categoryService.getCategoryById(request.categoryId());
        PaymentMethod paymentMethod = paymentMethodsService.findPaymentMethod(request.paymentMethodId());
        User currentAuthenticatedUser = authenticationService.getCurrentAuthenticatedUser();
        Card card = cardService.findCardById(Long.valueOf(request.cardId()));
        Currency currency = currencyService.findCurrency(request.currencyId());

        // First step, create installment
        Installment installment = new Installment();
        installment.setAmount(BigDecimal.valueOf(request.amount()));
        installment.setSplits(request.splits());
        installment.setDescription(request.description());
        installment.setPaymentMethod(paymentMethod);
        installment.setUser(currentAuthenticatedUser);
        installment.setCard(card);
        installment.setFirstSplitDate(request.firstSplitDate());
        installment.setCurrency(currency);
        installment = installmentRepository.save(installment);

//        // Calculate the amount
//        BigDecimal bigDecimalAmount = BigDecimal.valueOf(request.amount());
//        BigDecimal splitAmount = bigDecimalAmount.divide(
//                new BigDecimal(request.splits()),
//                2,
//                RoundingMode.HALF_UP
//        );

        return generateExpensesFromInstallment(request.firstSplitDate(),
                request.description(),
                installment, currentAuthenticatedUser,
                category, request.amount(),
                paymentMethod, currency, card);

    }

    private List<Expense> generateExpensesFromInstallment(LocalDate firstSplitDate, String description,
                                      Installment installment, User user, Category category,
                                      Double fullAmount, PaymentMethod paymentMethod, Currency currency, Card card) {


        BigDecimal bigDecimalAmount = BigDecimal.valueOf(fullAmount);
        BigDecimal splitAmount = bigDecimalAmount.divide(
                new BigDecimal(installment.getSplits()),
                2, RoundingMode.HALF_UP
        );


        List<Expense> expensesToCreate = new ArrayList<>();
        for (int i = 0; i < installment.getSplits(); i++) {
            Expense expense = new Expense();
            expense.setInstallment(installment);
            expense.setUser(user);
            expense.setCategory(category);
            expense.setAmount(splitAmount);
            expense.setDate(firstSplitDate.plusMonths(i));
            expense.setPaymentMethod(paymentMethod);
            expense.setCurrency(currency);
            expense.setCard(card);
            expense.setExtraInfo(description);
            expensesToCreate.add(expense);
        }

        return expenseRepository.saveAll(expensesToCreate);
    }

    @Transactional
    public void convertToInstallment(InstallmentConversionRequest request) {

        // First, delete the existent expense
        Expense expense = expenseService.findExpenseById(request.expenseId());

        if (expense == null)
            throw new ResourceNotFoundException("Expense with id " + request.expenseId() + " not found!!");

        expenseService.deleteExpense(expense.getId());

        User user = authenticationService.getCurrentAuthenticatedUser();
        Category category = categoryService.getCategoryById(request.categoryId());
        PaymentMethod paymentMethod = paymentMethodsService.findPaymentMethod(request.paymentMethodId());
        Currency currency = currencyService.findCurrency(request.currencyId());
        Card card = cardService.findCardById(request.cardId());

        Installment installment = new Installment();
        installment.setFirstSplitDate(request.date());
        installment.setAmount(BigDecimal.valueOf(request.amount()));
        installment.setDescription(request.extraInfo());
        installment.setPaymentMethod(paymentMethod);
        installment.setUser(user);
        installment.setSplits(request.splits());
        if (request.cardId() != null)
            installment.setCard(card);
        installment.setCurrency(currency);

        installment = installmentRepository.save(installment);

        var generatedExpenses = generateExpensesFromInstallment(request.date(), request.extraInfo(), installment, user, category,
                request.amount(), paymentMethod, currency, card);

        log.debug("Generated {} expenses from installment", generatedExpenses.size());


    }

}