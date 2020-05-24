package com.kakaopay.event.coupon.service;

import com.kakaopay.event.coupon.domain.entity.Coupon;
import com.kakaopay.event.coupon.domain.enums.CouponStatus;
import com.kakaopay.event.coupon.repository.CouponRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
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

    @AfterEach
    void tearDown() {
        couponRepository.deleteAll();
    }

    @Test
    @DisplayName("[S] 쿠폰을 잘 생성하고, 생성 Flag로 할당되는가")
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
    @DisplayName("[S] 쿠폰을 잘 가져오는가")
    void getCoupon() {
        String targetCouponSerial = UUID.randomUUID().toString();

        Coupon coupon = new Coupon();
        coupon.setCoupon(targetCouponSerial);

        coupon.setStatus(CouponStatus.CREATE);
        coupon.setExpiredTimestamp(LocalDateTime.now().plusHours(1));
        coupon.setRegTimestamp(LocalDateTime.now());

        couponRepository.saveAndFlush(coupon);

        Coupon coupon1 = couponService.getCoupon(targetCouponSerial);
        assertNotNull(coupon1);
        assertEquals(coupon1.getStatus(), coupon.getStatus());

    }

    @Test
    @DisplayName("[S] 쿠폰을 잘 할당하는가")
    void assignCoupon() {
        String targetCouponSerial = UUID.randomUUID().toString();

        Coupon coupon = new Coupon();
        coupon.setCoupon(targetCouponSerial);

        coupon.setStatus(CouponStatus.CREATE);
        coupon.setExpiredTimestamp(LocalDateTime.now().plusHours(1));
        coupon.setRegTimestamp(LocalDateTime.now());

        couponRepository.saveAndFlush(coupon);
        assertTrue(couponService.assignCoupon(targetCouponSerial));
        assertEquals(couponRepository.findFirstByCouponEquals(targetCouponSerial).getStatus(), CouponStatus.ASSIGN);

    }

    @Test
    @DisplayName("[S] 쿠폰을 잘 취소하는가")
    void cancel() {
        String targetCouponSerial = UUID.randomUUID().toString();

        Coupon coupon = new Coupon();
        coupon.setCoupon(targetCouponSerial);

        coupon.setStatus(CouponStatus.USE);
        coupon.setExpiredTimestamp(LocalDateTime.now().plusHours(1));
        coupon.setRegTimestamp(LocalDateTime.now());

        couponRepository.saveAndFlush(coupon);
        assertTrue(couponService.cancel(targetCouponSerial));
        assertEquals(couponRepository.findFirstByCouponEquals(targetCouponSerial).getStatus(), CouponStatus.ASSIGN);
    }

    @Test
    @DisplayName("[S] 쿠폰을 잘 사용하는가")
    void use() {
        String targetCouponSerial = UUID.randomUUID().toString();

        Coupon coupon = new Coupon();
        coupon.setCoupon(targetCouponSerial);

        coupon.setStatus(CouponStatus.ASSIGN);
        coupon.setExpiredTimestamp(LocalDateTime.now().plusHours(1));
        coupon.setRegTimestamp(LocalDateTime.now());

        couponRepository.saveAndFlush(coupon);
        assertTrue(couponService.use(targetCouponSerial));
        assertEquals(couponRepository.findFirstByCouponEquals(targetCouponSerial).getStatus(), CouponStatus.USE);
    }

    @Test
    @DisplayName("[S] 만료된 쿠폰만 가져와지는가")
    void expired() {

        String targetCouponSerial = UUID.randomUUID().toString();
        Coupon coupon = new Coupon();
        coupon.setCoupon(targetCouponSerial);

        coupon.setStatus(CouponStatus.ASSIGN);
        coupon.setExpiredTimestamp(LocalDateTime.now());
        coupon.setRegTimestamp(LocalDateTime.now());
        couponRepository.saveAndFlush(coupon);
        List<Coupon> expiredCoupons = couponService.getTodayExpiredList();
        assertNotNull(expiredCoupons);
        assertEquals(expiredCoupons.size(), 1);
    }
}