package com.github.Finance.controllers.api;

import com.github.Finance.dtos.requests.InstallmentPurchaseRequest;
import com.github.Finance.dtos.requests.SimpleExpenseCreationRequest;
import com.github.Finance.dtos.response.ExpenseResponse;
import com.github.Finance.services.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpensesApiController {

    private final ExpenseService expenseService;

    public ExpensesApiController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("")
    public ResponseEntity<?> getExpenses() {
        List<ExpenseResponse> expenses = expenseService.getUserExpensesByPeriodOfTime(null, LocalDate.now().minusDays(45), LocalDate.now())
                .stream()
                .map(ExpenseResponse::new)
                .toList();
        return ResponseEntity.ok(
            expenses
        );
    }

    @PostMapping("/simple-expense")
    public ResponseEntity<?> postExpenses(@RequestBody SimpleExpenseCreationRequest expensesCreationRequest) {

        var expense = expenseService.saveSimpleExpense(expensesCreationRequest);

        return ResponseEntity.ok(
            new ExpenseResponse(expense)
        );
    }

    @PostMapping("/installment-expense")
    public ResponseEntity<?> newInstallmentExpenses(@RequestBody InstallmentPurchaseRequest request) {
        var expenses = expenseService.createNewInstallmentExpense(request);
        return ResponseEntity.ok(
            expenses.stream()
            .map(ExpenseResponse::new)
            .toList()
        );
    }

}
