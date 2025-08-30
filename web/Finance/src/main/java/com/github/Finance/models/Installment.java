package com.github.Finance.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "installments")
@Entity
@Getter
@Setter
public class Installment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private int splits;

    private String description;

    @JoinColumn(name = "payment_method_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id"
    )
    private User user;

    @ManyToOne
    @JoinColumn(
        name = "card_id"
    )
    private Card card;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDate createdAt;

    @Column(name = "first_split_date")
    private LocalDate firstSplitDate;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

}
