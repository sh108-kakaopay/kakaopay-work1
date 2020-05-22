package com.kakaopay.event.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.kakaopay.event.coupon.domain.entity.Coupon;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {
    Coupon findFirstByCouponEquals(String uuid);

    List<Coupon> findByExpiredTimestampLessThanEqual(LocalDateTime now);
}