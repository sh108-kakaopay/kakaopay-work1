package com.kakaopay.event.coupon.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.kakaopay.event.coupon.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtUtil {
    private final JwtConfig jwtConfig;
    private final byte[] key;
    private final Verification jwtVerification;

    @Autowired
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.key = jwtConfig.getSecret().getBytes();
        this.jwtVerification = JWT.require(Algorithm.HMAC512(key));
    }

    public DecodedJWT decode(String token) {
        return jwtVerification.build().verify(token);
    }

    public String encode(String username, LocalDateTime expiredTimestamp) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(Date.from(expiredTimestamp.atZone(Clock.systemDefaultZone().getZone()).toInstant()))
                .sign(Algorithm.HMAC512(key));
    }

    public LocalDateTime buildExpiredTimestamp() {
        return LocalDateTime.now().plusHours(jwtConfig.getTokenAvailableHour());
    }
}
