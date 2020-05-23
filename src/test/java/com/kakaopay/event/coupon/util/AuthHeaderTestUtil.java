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
        return "Bearer " + jwtUtil.encode("yangs", LocalDateTime.now().plusHours(1));
    }
}
