package com.github.Finance.repositories;

import com.github.Finance.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.Finance.models.Currency;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {


    Optional<Currency> findByCurrencyFlag(String currencyFlag);

    @Query(value = "" +
    "SELECT\n" +
    "    c.id,\n" +
    "    c.currency_name,\n" +
    "    c.currency_flag,\n" +
    "    c.currency_symbol,\n" +
    "    c.decimal_places,\n" +
    "    COALESCE(expense_counts.count_expenses, 0) AS total_expenses_for_currency\n" +
    "FROM\n" +
    "    currencies c\n" +
    "LEFT JOIN (\n" +
    "    SELECT\n" +
    "        currency_id,\n" +
    "        COUNT(id) AS count_expenses\n" +
    "    FROM\n" +
    "        expenses\n" +
    "    WHERE user_id = :#{#user.id}\n" +
    "    GROUP BY\n" +
    "        currency_id\n" +
    ") AS expense_counts ON c.id = expense_counts.currency_id\n" +
    "LEFT JOIN\n" +
    "    users u ON u.id = :#{#user.id}\n" +
    "WHERE u.id IS NOT NULL\n" +
    "ORDER BY\n" +
    "    CASE\n" +
    "        WHEN c.id = u.preferred_currency THEN 0\n" +
    "        ELSE 1\n" +
    "    END ASC,\n" +
    "    COALESCE(expense_counts.count_expenses, 0) DESC,\n" +
    "    c.currency_name ASC;", nativeQuery = true)
    List<Currency> findAllUserCurrenciesByExpense(@Param("user") User user);

    @Query(value = "" +
    "SELECT\n" +
    "    c.id,\n" +
    "    c.currency_name,\n" +
    "    c.currency_flag,\n" +
    "    c.currency_symbol,\n" +
    "    c.decimal_places,\n" +
    "    COALESCE(incomes_count.count_incomes, 0) AS total_expenses_for_currency\n" +
    "FROM\n" +
    "    currencies c\n" +
    "LEFT JOIN (\n" +
    "    SELECT\n" +
    "        currency_code,\n" +
    "        COUNT(id) AS count_incomes\n" +
    "    FROM\n" +
    "        incomes\n" +
    "    WHERE user_id = :#{#user.id}\n" +
    "    GROUP BY\n" +
    "        currency_code\n" +
    ") AS incomes_count ON c.id = incomes_count.currency_code\n" +
    "LEFT JOIN\n" +
    "    users u ON u.id = :#{#user.id}\n" +
    "WHERE u.id IS NOT NULL\n" +
    "ORDER BY\n" +
    "    CASE\n" +
    "        WHEN c.id = u.preferred_currency THEN 0\n" +
    "        ELSE 1\n" +
    "    END ASC,\n" +
    "    COALESCE(incomes_count.count_incomes, 0) DESC,\n" +
    "    c.currency_name ASC;", nativeQuery = true)
    List<Currency> findAllUserCurrenciesByIncome(@Param("user")  User user);

    @Query("SELECT c FROM Currency c ORDER BY c.currencyFlag ASC ")
    List<Currency> findAllByAlphabeticalOrder();
}
