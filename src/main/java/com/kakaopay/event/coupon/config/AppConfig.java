package com.kakaopay.event.coupon.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("coupon.app")
public class AppConfig {
    public boolean batchEnable = false;
    public int batchTargetDay = 3;
}
