package com.github.Finance.services;

import com.github.Finance.dtos.UserIncomeSumDTO;
import com.github.Finance.dtos.forms.IncomeFilterForm;
import com.github.Finance.dtos.forms.RegisterIncomeForm;
import com.github.Finance.dtos.response.IncomesDetailResponse;
import com.github.Finance.models.Currency;
import com.github.Finance.models.Income;
import com.github.Finance.models.PaymentMethod;
import com.github.Finance.models.User;
import com.github.Finance.provider.currencyexchange.CurrencyExchangeProvider;
import com.github.Finance.repositories.IncomeRepository;
import com.github.Finance.specifications.IncomesSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    public List<Income> getUserMonthIncomes() {
        User user = authenticationService.getCurrentAuthenticatedUser();
        LocalDate today = LocalDate.now();
        return incomeRepository.findAllIncomesByMonth(user, today.getMonthValue(), today.getYear());
    }

    /**
     *
     * Method responsible for calculating the total amount, given different income currencies, and by
     * the times that they needed it
     *
     * @param incomes the list of incomes
     * @return a map containing the user currency, and the total
     *
     * // TODO: implement user currency preference
     *
     */
    public UserIncomeSumDTO getIncomesSum(List<Income> incomes) {

        // again, hardcoding it for now
        String userCurrency = "BRL";
        Double sum = 0.0;

        for (Income income : incomes) {

            if (!income.getCurrency().getCurrencyFlag().equals(userCurrency)) {

                try {
                    sum += exchangeRateService.getExchangeRate(income.getCurrency().getCurrencyFlag(), userCurrency, income.getIncomeDate());
                } catch (Exception e) {
                    log.error("Error while getting exchange rate: [{}]!!", e.getMessage());
                    sum += income.getAmount().doubleValue();
                }

                continue;
            }

            sum += income.getAmount().doubleValue();

        }

        log.info("Sum for currency {}: [{}]", userCurrency, sum);

        return new UserIncomeSumDTO(userCurrency, sum);
    }

    public List<Currency> getUserListedCurrencies() {
        User user = authenticationService.getCurrentAuthenticatedUser();

        return incomeRepository.findDistinctCurrenciesByUser(user);
    }

    // Ajax Requests

    public IncomesDetailResponse getIncomesDetails(IncomeFilterForm form) {

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

        return null;
    }


}
