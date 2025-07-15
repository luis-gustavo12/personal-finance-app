package com.github.Finance.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.github.Finance.models.User;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class TokenService {

    private final Algorithm algorithm;

    public TokenService(@Value("${jwt.secret-key}") String secretKey) {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String generateToken(User user, String issuer, String subject, long expiration ) {

        DecodedJWT jwt;
        Date now = new Date();

        try {
            String token = JWT.create()
                    .withIssuer(issuer)
                    .withSubject(subject)
                    .withExpiresAt(new Date(now.getTime() + expiration))
                    .withIssuedAt(now)
                    .withClaim("userId", user.getId().toString())
                    .sign(algorithm);
            return token;
        } catch (Exception e) {
            log.error("Error on token generation: {}", e.getMessage());
            return null;
        }


    }

    public Long getIdFromToken(String token) {
        DecodedJWT jwt;

        try {

        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public String getSubjectFromToken(String token) {
        DecodedJWT jwt;

        try {
            jwt = JWT.decode(token);
            String subject = jwt.getSubject();
            log.info("Subject: {}", subject);
            return subject;
        } catch (Exception e) {
            log.error("Error on token generation: {}", e.getMessage());
        }

        return null;
    }

}
