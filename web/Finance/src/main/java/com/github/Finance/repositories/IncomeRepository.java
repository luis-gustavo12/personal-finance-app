package com.github.Finance.repositories;

import com.github.Finance.models.Currency;
import com.github.Finance.models.Income;
import com.github.Finance.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long>, JpaSpecificationExecutor<Income> {
    List<Income> findAllByUser(User user);

    @Query("SELECT i FROM Income i WHERE i.user = :user AND YEAR(i.createdAt) = :year AND MONTH(i.createdAt) = :month")
    List<Income> findAllIncomesByMonth(
        @Param("user") User user,
        @Param("month") int month,
        @Param("year") int year
    );

    @Query("SELECT DISTINCT i.currency FROM Income i WHERE i.user = :user")
    List<Currency> findDistinctCurrenciesByUser(User user);

    @Query("SELECT i FROM Income i WHERE i.user = :user AND i.incomeDate BETWEEN :startDate AND :endDate")
    List<Income> findIncomesByPeriodOfTime(User user, LocalDate startDate, LocalDate endDate);

    List<Income> findTop15ByUserOrderByIncomeDateDesc(User user);
}
