package com.github.Finance.repositories;

import java.time.LocalDate;
import java.util.List;

import com.github.Finance.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.Finance.models.Subscription;
import com.github.Finance.models.User;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByUser(User user);

    @Query("SELECT s FROM Subscription s WHERE s.dayOfCharging = :date")
    List<Subscription> findTodayChargedSubscriptions(Byte date);
}
