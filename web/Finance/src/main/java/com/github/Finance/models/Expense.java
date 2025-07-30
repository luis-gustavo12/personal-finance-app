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
    
}
