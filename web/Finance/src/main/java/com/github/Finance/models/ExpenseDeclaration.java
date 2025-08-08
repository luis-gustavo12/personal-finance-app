package com.github.Finance.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses_declarations")
@Getter
@Setter
public class ExpenseDeclaration {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id"
    )
    private User user;

    private BigDecimal amount;

    private String info;

    @ManyToOne
    @JoinColumn(
        name = "category_id"
    )
    private Category category;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "payment_method_id"
    )
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(
        name = "currency_id"
    )
    private Currency currency;

    private LocalDate date;

}
