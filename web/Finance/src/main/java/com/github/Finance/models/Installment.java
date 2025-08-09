package com.github.Finance.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

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

}
