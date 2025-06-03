package com.github.Finance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.Finance.models.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    
}
