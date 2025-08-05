package com.github.Finance.jobs;


import com.github.Finance.services.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExpenseConversionJob extends QuartzJobBean {


    private final ExpenseService expenseService;

    public ExpenseConversionJob(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException { // <-- 3. Override executeInternal
        log.info("Executing misfired or scheduled job: ExpenseConversionJob");
        try {
            expenseService.generateExpenseFromSubscription();
            log.info("ExpenseConversionJob finished successfully.");
        } catch (Exception e) {
            log.error("Exception occurred while executing ExpenseConversionJob", e);
            // This tells Quartz the job failed and it should handle it based on the trigger's policy
            throw new JobExecutionException(e);
        }
    }
}
