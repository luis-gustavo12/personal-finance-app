package com.github.Finance.controllers.api;

import com.github.Finance.models.Currency;
import com.github.Finance.services.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyApiController {


    private final CurrencyService currencyService;

    public CurrencyApiController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping()
    public List<Currency> getCurrencies() {
        return currencyService.findAllCurrenciesByAlphabeticalOrder();
    }

}
