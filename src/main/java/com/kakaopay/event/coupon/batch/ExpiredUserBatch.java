package com.kakaopay.event.coupon.batch;

import com.kakaopay.event.coupon.config.AppConfig;
import com.kakaopay.event.coupon.service.CouponService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class ExpiredUserBatch {
    private final AppConfig appConfig;
    private final CouponService couponService;


    @Scheduled(cron = "00 09 * * * *")
    public void run() {
        if (appConfig.isBatchEnable() == false) {
            return;
        }

        if (couponService.sendExpiredMessage(appConfig.getBatchTargetDay()) == false) {
            log.info(String.format("Success Send Expired Message (target)  : %d", appConfig.getBatchTargetDay()));
        } else {
            log.info(String.format("Failed Send Expired Message (target)  : %d", appConfig.getBatchTargetDay()));
        }

    }
}
