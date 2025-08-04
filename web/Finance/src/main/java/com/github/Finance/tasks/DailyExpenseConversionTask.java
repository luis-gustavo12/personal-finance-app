package com.github.Finance.tasks;

import com.github.Finance.services.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DailyExpenseConversionTask {

    private final ExpenseService expenseService;

    public DailyExpenseConversionTask(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void generateExpenseOnSubscription() {

        try {
            log.info("Starting monthly report task");
            expenseService.generateExpenseFromSubscription();
        } catch (Exception e) {
            log.error("Exception occurred while executing task", e);
        }


    }
}
