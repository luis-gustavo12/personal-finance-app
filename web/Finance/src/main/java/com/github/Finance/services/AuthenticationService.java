package com.github.Finance.services;

import com.github.Finance.dtos.forms.LoginForm;
import com.github.Finance.models.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Class dedicated to authenticating users. Do not mix it with UserService!!
 * This class is intended to interact with UserService, to reach user database
 */
@Service
public class AuthenticationService {
    
    private final UserService userService;

    public AuthenticationService (UserService userService) {
        this.userService = userService;
    }

    public boolean authenticateUser(LoginForm loginForm) {


        return false;

    }

    public User getAuthenticatedUser() {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userService.getUserByEmail( userDetails.getUsername() );

    }




}
