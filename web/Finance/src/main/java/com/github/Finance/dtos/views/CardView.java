package com.github.Finance.dtos.views;

import java.time.LocalDate;

import com.github.Finance.enums.CardType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class CardView {
    private Long id;
    private String cardName;
    private int expirationMonth;
    private int expirationYear;
    private String lastDigits;
    private String brand;
    private CardType cardType;
}


