package com.github.Finance.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * This is one extension of the Expenses table, since for every expenses that is used a credit card,
 * there are different data used from a regular Cash expense, or even Brazilian PIX.
 *
 */
@Entity
@Table(name = "card_expenses")
@Getter
@Setter
@NoArgsConstructor
public class CardExpense {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
        name = "expense_id"
    )
    private Expense expense;

    @ManyToOne
    @JoinColumn(
        name = "card_id"
    )
    private Card card;
    
    @Column(name = "is_installment")
    private boolean isInstallment;

    @Column(name = "splits")
    private int splits;

    @Column(name = "has_interest_rate")
    private boolean hasInterestRate;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "amount_without_interest")
    private BigDecimal amountWithoutInterestRate;


    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM ('PENDING', 'PAID', 'CANCELLED')")
    private ExpenseCardEnum status;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;


}
