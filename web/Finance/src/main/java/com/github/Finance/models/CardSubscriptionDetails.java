package com.github.Finance.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "card_subscription_details")
@Getter
@Setter
public class CardSubscriptionDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
        name = "subscription_id"
    )
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(
        name = "card_id"
    )
    private Card card;

}
