package com.github.Finance.tasks;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MonthlyReportTask {

    public MonthlyReportTask() {

    }

    @Scheduled(cron = "0 0 0 2 * ?")
    public void execute() {
        try {
            log.info("Starting monthly report task");
        } catch (Exception ex) {
            log.error("Exception occurred while executing task", ex);
        }

    }

}
