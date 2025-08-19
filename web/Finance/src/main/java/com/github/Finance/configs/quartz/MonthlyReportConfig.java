package com.github.Finance.configs.quartz;

import com.github.Finance.jobs.MonthlyReportJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonthlyReportConfig {

    @Bean
    public JobDetail monthlyReportJobDetail() {

        return JobBuilder.newJob(MonthlyReportJob.class)
                .withIdentity("monthlyReportJob")
                .storeDurably()
                .withDescription("Monthly Report Job for sum of expenses and incomes")
                .build();

    }

    @Bean
    public Trigger monthlyReportTrigger(JobDetail jobDetail) {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule("0 0 2 1 * ?")
                .withMisfireHandlingInstructionFireAndProceed();
                ;

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("monthlyReportTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

}
