package com.kakaopay.event.coupon.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthHeaderTestUtil {
    @Autowired
    private JwtUtil jwtUtil;

    public String headerName() {
        return "Authorization";
    }

    public String headerValue() {
        return this.headerValue(LocalDateTime.now().plusHours(1));
    }

    public String headerValue(LocalDateTime expired) {
        return "Bearer " + jwtUtil.encode("yangs", expired);
    }
}
