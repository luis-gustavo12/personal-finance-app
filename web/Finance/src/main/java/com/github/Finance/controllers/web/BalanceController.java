package com.github.Finance.controllers.web;

import com.github.Finance.dtos.requests.BalanceReportRequest;
import com.github.Finance.services.BalanceService;
import com.github.Finance.services.CategoryService;
import com.github.Finance.services.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/balance")
public class BalanceController {

    private final CurrencyService currencyService;
    private final CategoryService categoryService;
    private final BalanceService balanceService;

    public BalanceController(CurrencyService currencyService, CategoryService categoryService, BalanceService balanceService) {
        this.currencyService = currencyService;
        this.categoryService = categoryService;
        this.balanceService = balanceService;
    }

    @GetMapping("")
    public String balance(Model model){
        model.addAttribute("currencies", currencyService.findAllCurrencies());
        model.addAttribute("categories", categoryService.getAllCurrentLoggedUserCategories());
        return "balance";
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterBalance(@RequestBody BalanceReportRequest request){
        try {
            return ResponseEntity.ok(balanceService.getBalanceReport(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("Error", e.getMessage())
            );
        }
    }

}
