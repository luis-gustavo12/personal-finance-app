package com.github.Finance.services;

import com.github.Finance.models.Report;
import com.github.Finance.models.User;
import com.github.Finance.repositories.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }


    public Report saveReport(Report report) {

        return reportRepository.save(report);

    }

    public List<Report> getReports(User user) {
        return reportRepository.findByUser(user);
    }



}
