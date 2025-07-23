package com.github.Finance.services;

import com.github.Finance.dtos.UserIncomeSumDTO;
import com.github.Finance.dtos.forms.IncomeFilterForm;
import com.github.Finance.dtos.forms.RegisterIncomeForm;
import com.github.Finance.dtos.response.IncomesDetailResponse;
import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.mappers.IncomesMapper;
import com.github.Finance.models.Currency;
import com.github.Finance.models.Income;
import com.github.Finance.models.PaymentMethod;
import com.github.Finance.models.User;
import com.github.Finance.provider.currencyexchange.CurrencyExchangeProvider;
import com.github.Finance.repositories.IncomeRepository;
import com.github.Finance.specifications.IncomesSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IncomesService {

    private final IncomeRepository incomeRepository;
    private final PaymentMethodsService paymentMethodsService;
    private final CurrencyService currencyService;
    private final AuthenticationService authenticationService;
    private final ExchangeRateService exchangeRateService;

    public IncomesService(IncomeRepository incomeRepository, PaymentMethodsService paymentMethodsService, CurrencyService currencyService, AuthenticationService authenticationService, CurrencyExchangeProvider currencyExchangeProvider, ExchangeRateService exchangeRateService) {
        this.incomeRepository = incomeRepository;
        this.paymentMethodsService = paymentMethodsService;
        this.currencyService = currencyService;
        this.authenticationService = authenticationService;
        this.exchangeRateService = exchangeRateService;
    }

    public Income findIncomeById(Long id) {
        return incomeRepository.findById(id).orElse(null);
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethodsService.getAllPaymentMethods();
    }

    public List<Currency> getCurrencies() {
        return currencyService.findAllCurrencies();
    }

    public void createIncome(RegisterIncomeForm form) {

        Income income = new Income();
        income.setAmount(BigDecimal.valueOf(form.incomeAmount()));
        income.setCurrency(currencyService.findCurrency(form.currencyId()));
        income.setPaymentMethod(paymentMethodsService.findPaymentMethod(form.paymentMethodId()));
        income.setIncomeDate(form.incomeDate());
        income.setUser(authenticationService.getCurrentAuthenticatedUser());
        income.setDescription(form.incomeDescription());
        income = incomeRepository.save(income);

        log.info("New income created!!");

    }

    public List<Income> getCurrentMonthUserIncomes() {
        User user = authenticationService.getCurrentAuthenticatedUser();
        LocalDate today = LocalDate.now();
        return incomeRepository.findAllIncomesByMonth(user, today.getMonthValue(), today.getYear());
    }

    public List<Income> getMonthUserIncomes(int month, int year ,User user) {
        return incomeRepository.findAllIncomesByMonth(user, month, year);
    }

    /**
     * Calculates the total value of a list of incomes, converting all amounts into a single target currency.
     * <p>
     * This method is designed to be both correct and efficient. Instead of iterating through every single
     * income and performing a currency conversion for each one (which is slow), it uses a grouping strategy:
     * <ol>
     * <li>It first groups all incomes by their currency (e.g., all "USD" incomes together, all "BRL" together).</li>
     * <li>It then calculates a subtotal for each currency group.</li>
     * <li>Finally, it converts each subtotal to the target currency and adds it to the grand total.</li>
     * </ol>
     * This approach ensures that currency conversion is performed only once per currency type, not per income,
     * and prevents calculation errors by never adding amounts of different currencies directly.
     *
     * @param incomes The list of Income objects to be processed.
     * @return A {@link UserIncomeSumDTO} containing the final calculated sum and the target currency string.
     *
     * @see UserIncomeSumDTO
     */
    public UserIncomeSumDTO getIncomesSum(List<Income> incomes) {

        // TODO: This should be fetched from the current user's preferences, not hardcoded.
        String userCurrency = "BRL";
        BigDecimal sum = BigDecimal.ZERO;

        Map<String, List<Income>> incomesByCurrency = incomes.stream()
            .collect(Collectors.groupingBy(income -> income.getCurrency().getCurrencyFlag()));



        for (Map.Entry<String, List<Income>> entry : incomesByCurrency.entrySet()) {
            String incomeCurrency = entry.getKey();
            List<Income> currentCurrencyIncomes =  entry.getValue();

            BigDecimal currentCurrencySum = currentCurrencyIncomes.stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (incomeCurrency.equals(userCurrency)) {
                currentCurrencySum = currentCurrencySum.add(currentCurrencySum);
            } else {
                // For converting a subtotal, using the current day's rate is a reasonable default.
                // This could be made more complex later if historical accuracy is needed.
                try {
                    Double conversionRate = exchangeRateService.getExchangeRate(incomeCurrency, userCurrency, LocalDate.now());

                    BigDecimal convertedSubtotal = currentCurrencySum.multiply(BigDecimal.valueOf(conversionRate));

                    sum = sum.add(convertedSubtotal);

                } catch (Exception e) {
                    // If conversion fails, we log the error and explicitly skip this amount
                    // to prevent corrupting the final sum with unconverted values.
                    log.error("Could not get exchange rate for {} to {}. Skipping this amount: [{} {}]",
                    incomeCurrency, userCurrency, incomeCurrency, currentCurrencySum);
                }

            }


        }

        log.info("Sum for currency {}: [{}]", userCurrency, sum);



        return new UserIncomeSumDTO(userCurrency, sum.doubleValue());
    }

    public List<Currency> getUserListedCurrencies() {
        User user = authenticationService.getCurrentAuthenticatedUser();

        return incomeRepository.findDistinctCurrenciesByUser(user);
    }

    // Ajax Requests

    public List<IncomesDetailResponse> getIncomesDetails(IncomeFilterForm form) {

        Specification<Income> user = IncomesSpecification.setUser(authenticationService.getCurrentAuthenticatedUser());
        Specification<Income> month = IncomesSpecification.hasMonth(form.month());
        Specification<Income> year = IncomesSpecification.hasYear(form.year());
        Specification<Income> currency = IncomesSpecification.hasCurrency(form.currencyFlag());
        Specification<Income> paymentMethods = IncomesSpecification.hasPaymentMethod(form.paymentMethodId());
        Specification<Income> min = IncomesSpecification.hasMinimum(form.minimumAmount());
        Specification<Income> max = IncomesSpecification.hasMaximum(form.maximumAmount());

        Specification<Income> specification = Specification
            .where(user)
            .and(month)
            .and(year)
            .and(currency)
            .and(paymentMethods)
            .and(min)
            .and(max);

        List<Income> incomes = incomeRepository.findAll(specification);

        return incomes.stream()
            .map(IncomesMapper::incomesToIncome)
            .collect(Collectors.toList());
    }

    public List<Income> getIncomesByUserAndPeriod(User user, LocalDate startDate, LocalDate endDate) {
        return incomeRepository.findIncomesByPeriodOfTime(user, startDate, endDate);
    }

    /**
     * Finds the income by Id, and also checks if it belongs to the authenticated User
     * @param id Desired id
     * @return The income
     */
    public Income findIncomeByIdForAuthenticatedUser(Long id) {
        Income income = incomeRepository.findById(id).orElse(null);

        if (income == null) {
            throw new ResourceNotFoundException("Income with id " + id + " not found");
        }
        User authenticatedUser = authenticationService.getCurrentAuthenticatedUser();

        if (!income.getUser().getId().equals(authenticatedUser.getId())) {
            log.info("IDOR detected");
            throw new SecurityException("Not authorized");
        }

        return income;
    }


    public Income updateExistentIncome(Long incomeId, RegisterIncomeForm form) {


        Income existingIncome = incomeRepository.findById(incomeId).orElse(null);

        if (existingIncome == null) {
            log.info("Income with id {} not found", incomeId);
            throw new ResourceNotFoundException("Income with id " + incomeId + " not found");
        }

        User authenticatedUser = authenticationService.getCurrentAuthenticatedUser();

        if (!existingIncome.getUser().getId().equals(authenticatedUser.getId())) {
            log.info("IDOR detected");
            throw new SecurityException("Not authorized");
        }

        existingIncome.setCurrency(currencyService.findCurrency(form.currencyId()));
        existingIncome.setAmount(BigDecimal.valueOf(form.incomeAmount()));
        existingIncome.setPaymentMethod(paymentMethodsService.findPaymentMethod(form.paymentMethodId()));
        existingIncome.setIncomeDate(form.incomeDate());
        existingIncome.setDescription(form.incomeDescription());

        return incomeRepository.save(existingIncome);

    }

    public void deleteIncomeById(Long id) {

        if (!incomeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Income with id " + id + " not found");
        }

        User authenticatedUser = authenticationService.getCurrentAuthenticatedUser();

        if (!findIncomeById(id).getUser().getId().equals(authenticatedUser.getId())) {
            throw new SecurityException("Not authorized");
        }

        incomeRepository.deleteById(id);
        log.info("Income with id {} deleted", id);

    }
}



