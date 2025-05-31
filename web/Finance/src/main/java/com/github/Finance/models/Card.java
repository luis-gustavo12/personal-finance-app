package com.github.Finance.models;

import java.time.LocalDateTime;

import com.github.Finance.enums.CardType;

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
import lombok.Setter;

@Table(name = "cards")
@Entity
@Getter
@Setter
public class Card {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "first_six_digits")
    private String firstSixDigits;

    @Column(name = "last_four_digits")
    private String lastFourDigits;

    @Column(name = "cardholder_name")
    private String cardholderName;

    @Column(name = "expiration_month")
    private int expirationMonth;

    @Column(name = "expiration_year")
    private int expirationYear;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private LocalDateTime createdAt;


    
}
