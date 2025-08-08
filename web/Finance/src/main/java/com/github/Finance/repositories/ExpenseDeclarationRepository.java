package com.github.Finance.repositories;

import com.github.Finance.models.ExpenseDeclaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseDeclarationRepository extends JpaRepository<ExpenseDeclaration, Long> {
}
