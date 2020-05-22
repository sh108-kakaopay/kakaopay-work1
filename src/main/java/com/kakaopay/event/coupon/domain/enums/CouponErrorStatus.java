package com.kakaopay.event.coupon.domain.enums;

public enum CouponErrorStatus {
    EXPIRED(-1000), NOT_ASSIGN(-1001), NOT_FOUND(-1002);
    public int value;

    CouponErrorStatus(int val) {
        value = val;
    }
}
