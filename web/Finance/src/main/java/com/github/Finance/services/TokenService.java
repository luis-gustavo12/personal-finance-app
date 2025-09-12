package com.github.Finance.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
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
    private final String issuer;
    private final JWTVerifier verifier;

    public TokenService(@Value("${jwt.secret-key}") String secretKey, @Value("${jwt.issuer}") String issuer) {
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.issuer = issuer;
        this.verifier = JWT.require(this.algorithm).withIssuer(issuer).build();
    }

    public String generateToken(User user ) {

        DecodedJWT jwt;
        Date now = new Date();

        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getEmail())
                    .withExpiresAt(new Date(now.getTime() + 3600 * 1000))
                    .withIssuedAt(now)
                    .withClaim("userId", user.getId().toString())
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("Error on token generation: {}", e.getMessage());
            return null;
        }


    }

    public DecodedJWT verifyToken(String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            log.error("Error on token verification: {}", e.getMessage());
            return null;
        }
    }

    public Long getIdFromToken(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        return (decodedJWT != null) ? decodedJWT.getClaim("userId").asLong() : null;
    }

    public String getSubjectFromToken(String token) {

        DecodedJWT jwt = verifyToken(token);
        return (jwt != null) ? jwt.getSubject() : "";
    }

}
