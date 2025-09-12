package com.github.Finance.controllers.api;

import com.github.Finance.dtos.forms.LoginForm;
import com.github.Finance.dtos.login.LoginResponse;
import com.github.Finance.models.User;
import com.github.Finance.services.AuthenticationService;
import com.github.Finance.services.TokenService;
import com.github.Finance.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginApiController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    public LoginApiController(UserService userService, AuthenticationService authenticationService, TokenService tokenService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
    }

    @GetMapping("")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping("")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {

        Optional<User> user = authenticationService.authenticate(loginForm);
        if (user.isPresent()) {
            String token = tokenService.generateToken(user.get());
            return ResponseEntity.ok()
                .body(
                    new LoginResponse(token, "Login successful!")
                );
        }

        return ResponseEntity.badRequest()
            .body(Map.of(
                "status", 400,
                "message", "Invalid username or password"
            ));

    }

}
