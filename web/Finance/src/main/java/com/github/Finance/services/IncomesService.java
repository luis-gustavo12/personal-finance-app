package com.github.Finance.services;

import com.github.Finance.dtos.UserIncomeSumDTO;
import com.github.Finance.dtos.forms.RegisterIncomeForm;
import com.github.Finance.models.Currency;
import com.github.Finance.models.Income;
import com.github.Finance.models.PaymentMethod;
import com.github.Finance.models.User;
import com.github.Finance.repositories.IncomeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class IncomesService {

    private final IncomeRepository incomeRepository;
    private final PaymentMethodsService paymentMethodsService;
    private final CurrencyService currencyService;
    private final AuthenticationService authenticationService;
    private final ExchangeRateService exchangeRateService;

    public IncomesService(IncomeRepository incomeRepository, PaymentMethodsService paymentMethodsService, CurrencyService currencyService, AuthenticationService authenticationService, ExchangeRateService exchangeRateService) {
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
                sum += exchangeRateService.handleRequest(income.getCurrency().getCurrencyFlag(), income.getIncomeDate());
                continue;
            }

            sum += income.getAmount().doubleValue();

        }

        log.info("Sum for currency {}: [{}]", userCurrency ,sum);;

        return new UserIncomeSumDTO(userCurrency, sum);
    }



}
