package com.github.Finance.controllers.api;

import com.github.Finance.dtos.requests.UpdateInstallmentRequest;
import com.github.Finance.dtos.response.ExpenseResponse;
import com.github.Finance.models.Expense;
import com.github.Finance.services.InstallmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/installments")
public class InstallmentsApiController {

    private final InstallmentService installmentService;

    public InstallmentsApiController(InstallmentService installmentService) {
        this.installmentService = installmentService;
    }


    @PutMapping("/{expenseId}")
    public ResponseEntity<?> updateInstallmentSeries(
            @PathVariable(name = "expenseId") Long id,
            @RequestBody UpdateInstallmentRequest request) {


        List<Expense> updatedExpenses = installmentService.updateInstallmentSeries(id, request);

        return ResponseEntity.ok(
                updatedExpenses.stream().map(ExpenseResponse::new).toList()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInstallmentSeries(@PathVariable Long id) {
        installmentService.deleteInstallmentSeries(id);
        return ResponseEntity.noContent().build();
    }

}
