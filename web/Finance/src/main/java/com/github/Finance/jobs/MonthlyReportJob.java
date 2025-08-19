package com.github.Finance.jobs;

import com.github.Finance.services.MonthlyReportService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MonthlyReportJob extends QuartzJobBean {

    private final MonthlyReportService monthlyReportService;

    public MonthlyReportJob(MonthlyReportService monthlyReportService) {
        this.monthlyReportService = monthlyReportService;
    }


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Monthly Report Job Started");
        try {
            monthlyReportService.generateMonthlyReport();
        }  catch (Exception e) {
            log.error("Monthly Report Job Failed", e);
        }
    }
}
