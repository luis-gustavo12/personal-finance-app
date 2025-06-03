package com.github.Finance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.Finance.models.SubscriptionTransaction;

@Repository
public interface SubscriptionTransactionRepository extends JpaRepository<SubscriptionTransaction, Long> {
    
}
