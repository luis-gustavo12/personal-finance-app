package com.github.Finance.services;

import com.github.Finance.dtos.forms.LoginForm;
import com.github.Finance.models.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Class dedicated to authenticating users. Do not mix it with UserService!!
 * This class is intended to interact with UserService, to reach user database
 */
@Service
public class AuthenticationService {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService (UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public User getCurrentAuthenticatedUser() {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userService.getUserByEmail( userDetails.getUsername() );

    }

    public Optional<User> authenticate(LoginForm loginForm) {

        User user = userService.getUserByEmail(loginForm.email());
        if (user == null) {
            return Optional.empty();
        }

        // Now see if the given password matches the email
        String userEncryptedPassword = user.getPassword();

        if (passwordEncoder.matches(loginForm.password(),  userEncryptedPassword)) {
            return Optional.of(user);
        }
        return Optional.empty();


    }


}
