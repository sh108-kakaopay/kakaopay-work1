package com.kakaopay.event.coupon.repository;

import com.kakaopay.event.coupon.domain.entity.Coupon;
import com.kakaopay.event.coupon.domain.enums.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {
    Coupon findFirstByCouponEquals(String uuid);

    List<Coupon> findByExpiredTimestampBetweenAndStatusEquals(LocalDateTime start, LocalDateTime end, CouponStatus status);

}