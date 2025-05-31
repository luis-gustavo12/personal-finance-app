package com.github.Finance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.Finance.models.CardExpense;

@Repository
public interface CardExpenseRepository extends JpaRepository<CardExpense, Long> {
    
}
