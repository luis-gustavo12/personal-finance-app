

package com.github.Finance.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.github.Finance.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    List<Expense> findByUser(User user);

    @Query("SELECT e FROM Expense e WHERE e.user =  :user AND YEAR(e.date) = :year AND " +
    "MONTH(e.date) = :month")
    List<Expense> findExpensesByMonthAndYear(User user, int month, int year);

    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.date BETWEEN :startDate AND :endDate ORDER BY e.date DESC")
    List<Expense> findExpensesByUserAndPeriod(User user, LocalDate startDate, LocalDate endDate);

    List<Expense> findExpensesByInstallment(Installment installment);

    @Transactional
    @Modifying
    @Query("DELETE FROM Expense e WHERE e.installment = :installment")
    int deleteExpenseByInstallment(Installment installment);

    Expense findFirstByInstallment(Installment installment);


    Optional<Expense> findFirstByInstallmentId(Long installmentId);

    @Modifying
    @Query("UPDATE Expense e SET e.extraInfo = :description WHERE e.installment.id = :installmentId")
    void updateDescriptionForInstallment(@Param("installmentId") Long installmentId, @Param("description") String description);

    @Modifying
    @Query("UPDATE Expense e SET e.category = :category WHERE e.installment.id = :installmentId AND e.date >= :today")
    void updateCategoryForFutureExpenses(@Param("installmentId") Long installmentId, @Param("category") Category category, @Param("today") LocalDate today);

    @Modifying
    @Query("UPDATE Expense e SET e.paymentMethod = :paymentMethod WHERE e.installment.id = :installmentId AND e.date >= :today")
    void updatePaymentMethodForFutureExpenses(@Param("installmentId") Long installmentId, @Param("paymentMethod") PaymentMethod paymentMethod, @Param("today") LocalDate today);

    @Modifying
    @Query("DELETE FROM Expense e WHERE e.installment.id = :installmentId")
    void deleteAllByInstallmentId(@Param("installmentId") Long installmentId);

}