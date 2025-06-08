package com.github.Finance.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "incomes")
@Getter
@Setter
@NoArgsConstructor
public class Income {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
        name = "user_id"
    )
    private User user;

    @ManyToOne
    @JoinColumn(name = "currency_code")
    private Currency currency;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(
        name = "payment_method_id"
    )
    private PaymentMethod paymentMethod;

    @Column(name = "date")
    private LocalDate incomeDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "income_description")
    private String description;


}


