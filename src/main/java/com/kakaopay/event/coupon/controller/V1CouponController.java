package com.kakaopay.event.coupon.controller;

import com.kakaopay.event.coupon.domain.entity.Coupon;
import com.kakaopay.event.coupon.domain.exception.InternalServerError;
import com.kakaopay.event.coupon.domain.response.V1CouponResultResponse;
import com.kakaopay.event.coupon.service.CouponService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/coupon")
@Slf4j
@Validated
public class V1CouponController {
    private final CouponService couponService;

    //3. 사용자에게 지급된 쿠폰을 조회하는 API를 구현하세요.
    @GetMapping("/{coupon-serial}")
    public V1CouponResultResponse index(@PathVariable("coupon-serial") @NotEmpty @Size(min = 36, max = 36) String couponSerial) {
        Coupon coupon = couponService.getCoupon(couponSerial);
        return new V1CouponResultResponse(coupon.getCoupon(), coupon.getExpiredTimestamp());
    }


    //2. 생성된 쿠폰중 하나를 사용자에게 지급하는 API를 구현하세요.
    @PutMapping("/{coupon-serial}/assign")
    public void allocate(@PathVariable("coupon-serial") @NotEmpty @Size(min = 36, max = 36) String couponSerial) {
        if (couponService.assignCoupon(couponSerial) == false) {
            throw new InternalServerError();
        }
    }

    //4. 지급된 쿠폰중 하나를 사용하는 API를 구현하세요. (쿠폰 재사용은 불가)
    @PutMapping("/{coupon-serial}/use")
    public void use(@PathVariable("coupon-serial") @NotEmpty @Size(min = 36, max = 36) String couponSerial) {
        if (couponService.use(couponSerial) == false) {
            throw new InternalServerError();
        }
    }

    //5. 지급된 쿠폰중 하나를 사용 취소하는 API를 구현하세요. (취소된 쿠폰 재사용 가능)
    @DeleteMapping("/{coupon-serial}/use")
    public void delete(@PathVariable("coupon-serial") @NotEmpty @Size(min = 36, max = 36) String couponSerial) {
        if (couponService.cancel(couponSerial) == false) {
            throw new InternalServerError();
        }
    }
}
