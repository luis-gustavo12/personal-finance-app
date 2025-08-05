package com.github.Finance.configs.quartz;



import com.github.Finance.jobs.ExpenseConversionJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Use @Configuration for clarity
public class ExpenseConversionConfig {

    @Bean
    public JobDetail jobDetail() {
        // Note: Job class is now your QuartzJobBean implementation
        return JobBuilder.newJob(ExpenseConversionJob.class)
                .withIdentity("expenseConversionJob") // Use camelCase for convention
                .storeDurably()
                .withDescription("Job to convert daily subscriptions into expenses")
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        // Your misfire instruction is correct.
        // MISFIRE_INSTRUCTION_FIRE_NOW is an alias for withMisfireHandlingInstructionFireAndProceed()
        // It means "fire immediately if a misfire is detected".
        CronScheduleBuilder scheduleBuilder =
                CronScheduleBuilder.cronSchedule("0 0 1 * * ?")
                        .withMisfireHandlingInstructionFireAndProceed();

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("expenseConversionTrigger")
                .withDescription("Trigger for the expense conversion job")
                .withSchedule(scheduleBuilder)
                .build();
    }
}