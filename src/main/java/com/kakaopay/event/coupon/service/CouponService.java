package com.kakaopay.event.coupon.service;

import com.kakaopay.event.coupon.domain.entity.Coupon;
import com.kakaopay.event.coupon.domain.enums.CouponErrorStatus;
import com.kakaopay.event.coupon.domain.enums.CouponStatus;
import com.kakaopay.event.coupon.domain.exception.CouponException;
import com.kakaopay.event.coupon.repository.CouponRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
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
        LocalDate now = LocalDate.now();
        LocalDateTime start = LocalDateTime.of(now, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(now, LocalTime.of(23, 59, 59));
        return couponRepository.findByExpiredTimestampBetweenAndStatusEquals(start, end, CouponStatus.ASSIGN);
    }

    public boolean sendExpiredMessage(int day) {
        LocalDateTime start = LocalDateTime.of(LocalDate.now().minusDays(day), LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(LocalDate.now().minusDays(day), LocalTime.of(23, 59, 59));
        List<Coupon> expiredTargets = couponRepository.findByExpiredTimestampBetweenAndStatusEquals(start, end, CouponStatus.ASSIGN);
        expiredTargets.forEach(item -> {
            log.info(String.format("Send Message : %s 티켓이 3일 뒤에 만료 됩니다.", item.getCoupon()));
        });

        return false;
    }
}
