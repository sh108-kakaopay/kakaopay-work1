package com.kakaopay.event.coupon.service;

import com.kakaopay.event.coupon.domain.entity.Coupon;
import com.kakaopay.event.coupon.domain.enums.CouponStatus;
import com.kakaopay.event.coupon.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CouponServiceTest {
    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;


    @Test
    void createCoupons() {
        LocalDateTime expired = LocalDateTime.now().plusHours(1);

        List<String> coupons = couponService.createCoupons(10, expired);
        assertEquals(coupons.size(), 10);
        for (String couponSerial : coupons) {
            Coupon coupon = couponRepository.findFirstByCouponEquals(couponSerial);
            assertNotNull(coupon);
            assertEquals(coupon.getStatus(), CouponStatus.CREATE);
            assertEquals(coupon.getExpiredTimestamp(), expired);
        }
    }

    @Test
    void getCoupon() {
        String targetCouponSerial = UUID.randomUUID().toString();

        Coupon coupon = new Coupon();
        coupon.setCoupon(targetCouponSerial);

        coupon.setStatus(CouponStatus.CREATE);
        coupon.setExpiredTimestamp(LocalDateTime.now().plusHours(1));
        coupon.setRegTimestamp(LocalDateTime.MAX);

        couponRepository.saveAndFlush(coupon);

        Coupon coupon1 = couponService.getCoupon(targetCouponSerial);
        assertNotNull(coupon1);
        assertEquals(coupon1.getStatus(), coupon.getStatus());

    }

    @Test
    void assignCoupon() {
    }

    @Test
    void cancel() {
    }

    @Test
    void use() {
    }
}