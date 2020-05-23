package com.kakaopay.event.coupon.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class V1AuthTokenResponse {
    public V1AuthTokenResponse() {

    }

    private String accessToken;
    private LocalDateTime expiredTimestamp;
}
