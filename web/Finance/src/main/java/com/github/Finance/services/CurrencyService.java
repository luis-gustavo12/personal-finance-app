package com.github.Finance.services;

import java.util.List;

import com.github.Finance.models.User;
import org.springframework.stereotype.Service;

import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.models.Currency;
import com.github.Finance.repositories.CurrencyRepository;

@Service
public class CurrencyService {

    private final CurrencyRepository repository;

    public CurrencyService (CurrencyRepository repository) {
        this.repository = repository;
    }


    public List<Currency> findAllCurrencies() {
        return repository.findAll();
    }

    /**
     * Find all the currencies given the user preferred currency, and ordered by the amount of expenses they have
     * Ideal for UX on forms
     * @return List of the currencies, ordered by their relevance
     *
     */
    public List<Currency> findAllCurrenciesByUserAndExpense(User user) {
        return repository.findAllUserCurrenciesByExpense(user);
    }

    public Currency findCurrency(Long id) {
        return repository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User could not be found!!!") );
    }

    public Currency findCurrencyByCurrencyFlag(String currencyFlag) {
        return repository.findByCurrencyFlag(currencyFlag).orElse(null);
    }

    /**
     * Find all currencies given the preferred user currency, and ordered by the amount of incomes they have
     * Also ideal for UX on forms
     */
    public List<Currency> findAllCurrenciesByUserAndIncome(User user) {
        return repository.findAllUserCurrenciesByIncome(user);
    }
    
}
