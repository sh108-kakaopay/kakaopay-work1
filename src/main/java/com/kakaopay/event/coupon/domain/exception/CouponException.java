package com.kakaopay.event.coupon.domain.exception;

import com.kakaopay.event.coupon.domain.enums.CouponErrorStatus;
import lombok.Getter;

@Getter
public class CouponException extends RuntimeException {
    private CouponErrorStatus status;

    public CouponException(CouponErrorStatus couponErrorStatus) {
        super();
        this.status = couponErrorStatus;
    }
}
