package com.github.Finance.controllers.api;

import com.github.Finance.dtos.forms.RegisterIncomeForm;
import com.github.Finance.dtos.requests.IncomeCreationRequest;
import com.github.Finance.services.IncomesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incomes")
@Slf4j
public class IncomesApiController {


    private final IncomesService incomesService;

    public IncomesApiController(IncomesService incomesService) {
        this.incomesService = incomesService;
    }

    @GetMapping("")
    public ResponseEntity<?> getUserIncomes () {
        return ResponseEntity.ok(
            incomesService.getUserRecentIncomes(null)
        );
    }

    @RequestMapping(value = "/edit/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateIncome(@PathVariable Long id, @RequestBody RegisterIncomeForm incomeForm) {
        incomesService.updateExistentIncome(id, incomeForm);
        return ResponseEntity.ok().build();
    }

    @PostMapping("")
    public ResponseEntity<?> addIncome(@RequestBody IncomeCreationRequest request) {
        incomesService.createIncome(request);
        return ResponseEntity.ok().build();
    }

}
