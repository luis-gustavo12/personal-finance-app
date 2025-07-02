package com.github.Finance.controllers.web;

import com.github.Finance.models.Report;
import com.github.Finance.models.User;
import com.github.Finance.services.AuthenticationService;
import com.github.Finance.services.MonthlyReportService;
import com.github.Finance.services.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@Slf4j
public class DashboardController {

    private final ReportService reportService;
    private final AuthenticationService authenticationService;
    private final MonthlyReportService monthlyReportService;

    public DashboardController(ReportService reportService, AuthenticationService authenticationService, MonthlyReportService monthlyReportService) {
        this.reportService = reportService;
        this.authenticationService = authenticationService;
        this.monthlyReportService = monthlyReportService;
    }


    @GetMapping("")
    public String mainDashboard(Model model) {

        User user = authenticationService.getCurrentAuthenticatedUser();

        List<Report> reports = reportService.getReports(user);

        if (reports.isEmpty()) {
            log.info("No report found for current user!!");
        }

        return "dashboard";
    }
    

    
}
