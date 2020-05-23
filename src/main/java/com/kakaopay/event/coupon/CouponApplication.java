package com.kakaopay.event.coupon;

import com.kakaopay.event.coupon.config.AppConfig;
import com.kakaopay.event.coupon.config.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableConfigurationProperties({JwtConfig.class, AppConfig.class})
@SpringBootApplication
public class CouponApplication {


    public static void main(String[] args) {
        SpringApplication.run(CouponApplication.class, args);
    }

}
