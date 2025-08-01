package com.github.Finance.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
public class Subscription {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(
        name = "user_id"
    )
    User user;

    @Column(name = "subscription_name", nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(
        name = "currency_id"
    )
    Currency currency;

    @Column(name = "subscription_cost")
    BigDecimal cost;

    @ManyToOne
    @JoinColumn(
        name = "payment_method_id"
    )
    PaymentMethod paymentMethod;

    @Column(name = "recurrence_rule")
    String recurrenceRule;

    @Column(name = "day_of_charging")
    Byte dayOfCharging;

    @Column(name = "valid_from", nullable = false)
    LocalDate validFrom;

    @Column(name = "valid_until")
    LocalDate validUntil;

    @Column(name = "created_at", insertable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
        name = "category_id"
    )
    Category category;


    
}
