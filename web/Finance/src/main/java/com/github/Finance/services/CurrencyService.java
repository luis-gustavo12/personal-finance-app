package com.github.Finance.services;

import java.util.List;

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

    public Currency findCurrency(Long id) {
        return repository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User could not be found!!!") );
    }
    
}
