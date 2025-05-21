package com.github.Finance.models;

import java.math.BigDecimal;

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

@Table
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

    
}
