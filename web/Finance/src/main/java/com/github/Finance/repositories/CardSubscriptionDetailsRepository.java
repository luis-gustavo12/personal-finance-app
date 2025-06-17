package com.github.Finance.repositories;

import com.github.Finance.models.CardSubscriptionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardSubscriptionDetailsRepository extends JpaRepository<CardSubscriptionDetails, Long> {
}
