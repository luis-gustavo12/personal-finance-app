package com.github.Finance.services;

import com.github.Finance.exceptions.ResourceNotFoundException;
import com.github.Finance.models.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class ExpenseDetailsService {

    private final AuthenticationService authenticationService;

    public ExpenseDetailsService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public void validateUserAccess(Long id) {

        User user = authenticationService.getCurrentAuthenticatedUser();

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (!id.equals(user.getId())) {
            throw new SecurityException("User not authorized to perform this action");
        }


    }

}
