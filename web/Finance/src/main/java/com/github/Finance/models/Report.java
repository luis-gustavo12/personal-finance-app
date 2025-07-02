package com.github.Finance.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (
    name = "currency"
    )
    private Currency currency;

    @ManyToOne
    @JoinColumn (
        name = "user_id"
    )
    private User user;

    @Column(name = "incomes_sum")
    private BigDecimal incomesSum;

    @Column(name = "expenses_sum")
    private BigDecimal expensesSum;

    @Column(name = "generation_date")
    private LocalDateTime generationDate;


}
