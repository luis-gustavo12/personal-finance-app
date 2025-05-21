package com.github.Finance.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "currencies")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Currency {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_name")
    private String currencyName;

    @Column(name = "currency_flag")
    private String currencyFlag;

    @Column(name = "currency_symbol")
    private String currencySymbol;

    @Column(name = "decimal_places")
    private int decimalPlaces;

}
