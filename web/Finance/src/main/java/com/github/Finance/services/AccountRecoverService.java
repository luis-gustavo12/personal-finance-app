package com.github.Finance.services;

import com.github.Finance.models.RecoverPasswordToken;
import com.github.Finance.models.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@Slf4j
public class AccountRecoverService {

    private final UserService userService;
    private final TokenService tokenService;
    private final RecoverPasswordTokenService recoverPasswordTokenService;
    private final EmailService emailService;
    private final HttpServletRequest httpServletRequest; // Injecting this, so that I can get the websites URL, without manual configuration

    public AccountRecoverService(UserService userService, TokenService tokenService, RecoverPasswordTokenService recoverPasswordTokenService, EmailService emailService, HttpServletRequest httpServletRequest) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.recoverPasswordTokenService = recoverPasswordTokenService;
        this.emailService = emailService;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     *
     * Method responsible for:
     * - Generating the token
     * - Ask the RecoverPasswordTokenService to store the token
     * - Send email to the users' inbox to store the token
     *
     * @param email e-mail as a string
     */
    public void generateRecoverToken(String email) {

        User user = userService.getUserByEmail(email);

        if (user == null) {
            log.error("User not found with email {}", email);
            return;
        }

        String token = tokenService.generateToken(
            user
        );

        recoverPasswordTokenService.storeToken(token, user);

        String appBaseUrl = ServletUriComponentsBuilder.fromRequestUri(httpServletRequest)
            .replacePath(null)
            .build().toUriString();

        log.debug("Base URL: {}", appBaseUrl);

        emailService.sendEmail(
            user.getEmail(), "Account Recover Confirmation",
            "<p>Please, confirm your new password through the following link: <a href=\""
            + appBaseUrl + "/recover/token/" + token + "\">"
            + appBaseUrl + "/recover/token/" + token  + "</a></p>"
        );

    }

    public void recoverAccountByToken(String token, String newPassword) {

        RecoverPasswordToken passwordToken = recoverPasswordTokenService.getToken(token);

        String subject = tokenService.getSubjectFromToken(token);

        User user = userService.getUserByEmail(subject);

        user = userService.updateUserPassword(user, newPassword);

        log.info("User {} has been recovered", user.getEmail());

    }

}
