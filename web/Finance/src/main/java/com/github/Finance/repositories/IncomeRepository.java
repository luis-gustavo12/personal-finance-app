package com.github.Finance.repositories;

import com.github.Finance.models.Currency;
import com.github.Finance.models.Income;
import com.github.Finance.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long>, JpaSpecificationExecutor<Income> {
    List<Income> findAllByUser(User user);

    @Query("SELECT i FROM Income i WHERE i.user = :user AND YEAR(i.incomeDate) = :year AND MONTH(i.incomeDate) = :month")
    List<Income> findAllIncomesByMonth(
        @Param("user") User user,
        @Param("month") int month,
        @Param("year") int year
    );

    @Query("SELECT DISTINCT i.currency FROM Income i")
    List<Currency> findDistinctCurrenciesByUser(User user);

}
