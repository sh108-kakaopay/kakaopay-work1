package com.kakaopay.event.coupon.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class V1CouponResultResponse {
    public V1CouponResultResponse() {

    }

    private String serial;
    private LocalDateTime expired;
}
