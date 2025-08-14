

package com.github.Finance.repositories;

import java.time.LocalDate;
import java.util.List;

import com.github.Finance.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.Finance.models.Expense;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    List<Expense> findByUser(User user);

    @Query("SELECT e FROM Expense e WHERE e.user =  :user AND YEAR(e.date) = :year AND " +
    "MONTH(e.date) = :month")
    List<Expense> findExpensesByMonthAndYear(User user, int month, int year);

    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.date BETWEEN :startDate AND :endDate")
    List<Expense> findExpensesByUserAndPeriod(User user, LocalDate startDate, LocalDate endDate);
}