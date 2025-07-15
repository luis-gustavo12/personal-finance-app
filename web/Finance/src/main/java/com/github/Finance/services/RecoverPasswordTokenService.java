package com.github.Finance.services;

import com.github.Finance.models.RecoverPasswordToken;
import com.github.Finance.models.User;
import com.github.Finance.repositories.RecoverPasswordTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RecoverPasswordTokenService {

    private final RecoverPasswordTokenRepository recoverPasswordTokenRepository;

    public RecoverPasswordTokenService(RecoverPasswordTokenRepository recoverPasswordTokenRepository, RecoverPasswordTokenRepository recoverPasswordTokenRepository1) {

        this.recoverPasswordTokenRepository = recoverPasswordTokenRepository1;
    }

    public void storeToken(String token, User user) {
        RecoverPasswordToken passwordToken = new RecoverPasswordToken();
        passwordToken.setToken(token);
        passwordToken.setUser(user);

        recoverPasswordTokenRepository.save(passwordToken);
        log.info("Token saved successfully");

    }

    public RecoverPasswordToken getToken(String token) {
        return recoverPasswordTokenRepository.findByToken(token);

    }

}
