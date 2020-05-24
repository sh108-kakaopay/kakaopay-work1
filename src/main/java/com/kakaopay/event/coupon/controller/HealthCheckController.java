package com.kakaopay.event.coupon.controller;

import com.kakaopay.event.coupon.domain.exception.InternalServerErrorException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    private boolean isOn = true;

    @GetMapping("/health")
    public void health() {
        if (isOn == false) {
            throw new InternalServerErrorException();
        }
    }

    @PutMapping("/health")
    public void healthChange() {
        isOn = !isOn;
    }
}
