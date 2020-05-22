package com.kakaopay.event.coupon.domain.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class V1CouponErrorResponse {
    public V1CouponErrorResponse() {}
    private int errorCode;
}
