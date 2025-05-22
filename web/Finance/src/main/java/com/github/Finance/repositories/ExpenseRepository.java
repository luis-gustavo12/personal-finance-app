

package com.github.Finance.repositories;

import java.util.List;

import com.github.Finance.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.Finance.models.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    

    
}