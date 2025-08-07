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
import lombok.Setter;

/**
 *
 * One of the most important, and also, more complex parts of the project, because every payment form
 * has its own peculiarity to be described as an expense. Also, there's another complexity layer
 * which is the Currency, so many calculations happen from time to time, which the application needs to be aware of
 *
 */
@Table(name = "expenses")
@Entity
@Getter
@Setter
public class Expense {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
        name = "payment_method_id"
    )
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(
        name = "currency_id"
    )
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal amount;

    // This is the actual texts that defines the expense. TODO: assign a better name
    @Column(name = "extra_info")
    private String extraInfo;

    @ManyToOne
    @JoinColumn(
        name = "user_id"
    )
    private User user;

    @Column(name = "expense_date", nullable = false)
    private LocalDate date;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
        name = "category_id"
    )
    private Category category;

    // Every expense can be assigned to a subscription. There's already a task, defined under
    // Quartz jobs, that runs every day, listing all the subscriptions, and creating one expense
    // for a subscription
    @ManyToOne
    @JoinColumn(
        name = "subscription_id"
    )
    private Subscription subscription;
    
}
