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

@Entity
@Table(name = "subscription_transactions")
@Getter
@Setter
@NoArgsConstructor
public class SubscriptionTransaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(
        name = "subscription_id"
    )
    Subscription subscription;


    // Even though it also exists on subscriptions table,
    // sometimes, payments can change, so basically in the transaction,
    // is where the actual paid value and payment form are set.
    // No problem if they don't match
    @ManyToOne
    @JoinColumn(
        name = "payment_method_id"
    )
    PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(
        name = "currency_id"
    )
    Currency currency;

    @Column(name = "amount_paid")
    BigDecimal amountPaid;

    @Column(name = "payment_date")
    LocalDate paymentDate;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    
}
