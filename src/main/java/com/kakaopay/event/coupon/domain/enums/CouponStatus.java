package com.kakaopay.event.coupon.domain.enums;

public enum CouponStatus {
    CREATE(1), ASSIGN(2), USE(3);
    public int value;

    CouponStatus(int val) {
        this.value = val;
    }

//    CREATE(0)
}
