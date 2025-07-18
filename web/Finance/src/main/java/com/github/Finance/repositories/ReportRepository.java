package com.github.Finance.repositories;

import com.github.Finance.models.Report;
import com.github.Finance.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


    List<Report> findByUser(User user);

}
