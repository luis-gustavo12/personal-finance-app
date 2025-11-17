package com.github.Finance.controllers.api;

import com.github.Finance.dtos.requests.*;
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
import java.util.Map;

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
     * @param id Current installment ID. To be deleted
     * @param request The needed data for adding a new simple expense
     * @return
     */
    @PostMapping("{installmentId}/convert-simple-expense")
    public ResponseEntity<?> convertToSimpleExpense(@PathVariable(name = "installmentId") Long id, @RequestBody SimpleExpenseConversionRequest request) {
        Expense expense = expenseService.convertInstallment(id, request);

        return ResponseEntity.ok(
                expenseResponseFactory.create(expense)
        );
    }

    /**
     * Convert one simple expense into an installment
     */
    @PostMapping("convert-to-installment")
    public ResponseEntity<?> convertToInstallment(@RequestBody InstallmentConversionRequest request) {

        installmentService.convertToInstallment(request);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Installment updated successfully!!"
                )
        );


    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {

        log.debug("Deleting expense {}", id);

        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();



    }



}
