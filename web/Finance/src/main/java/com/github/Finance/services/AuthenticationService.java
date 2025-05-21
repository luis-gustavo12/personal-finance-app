package com.github.Finance.services;

import com.github.Finance.dtos.forms.LoginForm;
import org.springframework.stereotype.Service;

/**
 * Class dedicated to authenticating users. Do not mix it with UserService!!
 * This class is intended to interact with UserService, to reach user database
 */
@Service
public class AuthenticationService {
    


    public boolean authenticateUser(LoginForm loginForm) {


        return false;

    }



}
