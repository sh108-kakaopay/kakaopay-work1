package com.kakaopay.event.coupon.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class V1ErrorMessage {
    private String message;
    private String traceId;
}
