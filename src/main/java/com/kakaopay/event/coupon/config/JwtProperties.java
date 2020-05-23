package com.kakaopay.event.coupon.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
//TODO : Spring Config로 가져오도록 수정
public class JwtProperties {
    public final String secret = "yangs";
    public final int tokenAvailableHour = 12; // 10 days
}
