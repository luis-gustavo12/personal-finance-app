

package com.github.Finance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.Finance.models.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    

    
}