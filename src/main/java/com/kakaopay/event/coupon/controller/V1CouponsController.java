package com.kakaopay.event.coupon.controller;

import com.kakaopay.event.coupon.domain.response.V1CouponResultResponse;
import com.kakaopay.event.coupon.service.CouponService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/coupons")
@Slf4j
@Validated
public class V1CouponsController {
    private final CouponService couponService;

    //1. 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관하는 API를 구현하세요.
    @PostMapping("")
    public List<String> createCoupon(@RequestParam("coupon-size") @Min(1) @Max(1000) int size,
                                     @RequestParam("expired-datetime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime expired) {
        return couponService.createCoupons(size, expired.withZoneSameInstant(ZoneOffset.systemDefault()).toLocalDateTime());
    }


    //6. 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회하는 API를 구현하세요.
    @GetMapping("/expired")
    public List<V1CouponResultResponse> expired() {
        return couponService.getTodayExpiredList()
                .stream()
                .map(item -> new V1CouponResultResponse(item.getCoupon(), item.getExpiredTimestamp()))
                .collect(Collectors.toList());
    }

}
