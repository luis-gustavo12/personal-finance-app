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

/**
 * Main entity responsible for representing cards data in a DB. For being PCI DSS compliant,
 * we don't handle the sensitive card data, and for now, Stripe does it for us. We just interact with their API,
 * and store the token. Even if in the future Stripe doesn't fit our needs, and a change for another Payment
 * Service Provider is required, I think there won't be necessary a change in the DB Schema, since they probably
 * use a token as well, and our application only would need to extract this token though the API.
 */

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

    @Column(name = "token")
    private String token;

    @Column(name = "card_description")
    private String cardDescription;

    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;


    
}
