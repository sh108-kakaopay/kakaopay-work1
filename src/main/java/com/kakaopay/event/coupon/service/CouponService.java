package com.kakaopay.event.coupon.service;

import com.kakaopay.event.coupon.domain.entity.Coupon;
import com.kakaopay.event.coupon.domain.enums.CouponErrorStatus;
import com.kakaopay.event.coupon.domain.enums.CouponStatus;
import com.kakaopay.event.coupon.domain.exception.CouponException;
import com.kakaopay.event.coupon.repository.CouponRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    @Transactional
    public List<String> createCoupons(int couponSize, LocalDateTime expiredTime) {
        LocalDateTime createTime = LocalDateTime.now();
        List<String> coupons = new ArrayList<>(couponSize);
        for (int i = 0; i < couponSize; i++) {
            String ticket = UUID.randomUUID().toString();
            Coupon coupon = new Coupon();
            coupon.setCoupon(ticket);
            coupon.setStatus(CouponStatus.CREATE);
            coupon.setRegTimestamp(createTime);
            coupon.setExpiredTimestamp(expiredTime);
            //TODO : Duplicate Exception 발생시 다른 UUID로 넣게 변경한다.
            if (couponRepository.save(coupon).getCouponId() != null) {
                coupons.add(ticket);
            }
        }
        return coupons;
    }

    public Coupon getCoupon(String uuid) throws CouponException {
        Coupon coupon = couponRepository.findFirstByCouponEquals(uuid);
        //TODO : 쿠폰이 없다.
        if (coupon == null) {
            throw new CouponException(CouponErrorStatus.NOT_FOUND);
        }
        //TODO : 만료되었다면 할당 할 수 없다.
        if (LocalDateTime.now().isAfter(coupon.getExpiredTimestamp())) {
            throw new CouponException(CouponErrorStatus.EXPIRED);
        }
        return coupon;
    }

    @Transactional
    public boolean assignCoupon(String uuid) throws CouponException {
        Coupon coupon = this.getCoupon(uuid);
        //TODO : 생성 상태가 아닌 쿠폰은 무시한다.
        if (coupon.getStatus() != CouponStatus.CREATE) {
            throw new CouponException(CouponErrorStatus.NOT_ASSIGN);
        }
        coupon.setStatus(CouponStatus.ASSIGN);
        return couponRepository.saveAndFlush(coupon).getStatus() == CouponStatus.ASSIGN;
    }

    @Transactional
    public boolean cancel(String uuid) throws CouponException {
        Coupon coupon = this.getCoupon(uuid);
        //TODO : 사용된 쿠폰에 대해서만 처리 한다.
        if (coupon.getStatus() != CouponStatus.USE) {
            throw new CouponException(CouponErrorStatus.NOT_ASSIGN);
        }
        coupon.setStatus(CouponStatus.ASSIGN);
        return couponRepository.saveAndFlush(coupon).getStatus() == CouponStatus.ASSIGN;
    }

    @Transactional
    public boolean use(String uuid) throws CouponException {
        Coupon coupon = this.getCoupon(uuid);
        if (coupon.getStatus() != CouponStatus.ASSIGN) {
            throw new CouponException(CouponErrorStatus.NOT_ASSIGN);
        }
        coupon.setStatus(CouponStatus.USE);
        return couponRepository.saveAndFlush(coupon).getStatus() == CouponStatus.USE;
    }

    public List<Coupon> getTodayExpiredList() {
        return couponRepository.findByExpiredTimestampLessThanEqual(LocalDateTime.now());
    }
}
