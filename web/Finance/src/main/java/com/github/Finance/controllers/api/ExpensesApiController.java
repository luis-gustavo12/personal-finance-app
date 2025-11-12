package com.github.Finance.controllers.api;

import com.github.Finance.dtos.requests.SimpleExpenseConversionRequest;
import com.github.Finance.dtos.requests.InstallmentPurchaseRequest;
import com.github.Finance.dtos.requests.SimpleExpenseCreationRequest;
import com.github.Finance.dtos.requests.UpdateExpenseRequest;
import com.github.Finance.dtos.response.ExpenseResponse;
import com.github.Finance.factories.ExpenseResponseFactory;
import com.github.Finance.models.Expense;
import com.github.Finance.services.ExpenseService;
import com.github.Finance.services.InstallmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@Slf4j
public class ExpensesApiController {

    private final ExpenseService expenseService;
    private final InstallmentService installmentService;
    private final ExpenseResponseFactory expenseResponseFactory;

    public ExpensesApiController(ExpenseService expenseService, InstallmentService installmentService, ExpenseResponseFactory expenseResponseFactory) {
        this.expenseService = expenseService;
        this.installmentService = installmentService;
        this.expenseResponseFactory = expenseResponseFactory;
    }

    @GetMapping("")
    public ResponseEntity<?> getExpenses() {
        List<Expense> expenses = expenseService.getUserExpensesByPeriodOfTime(null, LocalDate.now().minusDays(45), LocalDate.now());

        List<ExpenseResponse> convertedExpenses = expenseResponseFactory.createList(expenses);

        return ResponseEntity.ok(
            convertedExpenses
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
        var expenses = installmentService.createNewInstallmentExpense(request);
        return ResponseEntity.ok(
            expenses.stream()
            .map(ExpenseResponse::new)
            .toList()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody UpdateExpenseRequest request) {

        Expense expense = expenseService.updateSingleExpense(id, request);

        log.debug("Expense with id {} updated", expense.getId());

        return ResponseEntity.noContent().build();
    }

    /**
     * Method responsible for receiving one installment expense, and convert it to simple expense
     * @param id Current Expense ID
     * @param request The needed data for adding a new simple expense
     * @return
     */
    @PostMapping("{installmentId}/convert-simple-expense")
    public ResponseEntity<?> convertToSimpleExpense(@PathVariable(name = "installmentId") Long id, @RequestBody SimpleExpenseConversionRequest request) {
        return ResponseEntity.noContent().build();
    }



}
