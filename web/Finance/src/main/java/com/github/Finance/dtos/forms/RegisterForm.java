package com.github.Finance.dtos.forms;

public record RegisterForm (
        String firstName,
        String lastName,
        String email,
        String password,
        String currency
    )
{

    
}
