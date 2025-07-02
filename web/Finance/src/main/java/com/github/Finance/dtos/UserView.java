package com.github.Finance.dtos;

import com.github.Finance.models.Currency;
import com.github.Finance.models.User;

public record UserView(

    String email,
    String name, // firstName + lastName
    CurrencyDTO preferredCurrency
) {

    public UserView (User user){
        this(user.getEmail(), user.getFirstName() + user.getLastName(), new CurrencyDTO(user.getPreferredCurrency()));
    }

}
