package com.kakaopay.event.coupon.config;

import com.kakaopay.event.coupon.domain.exception.CouponException;
import com.kakaopay.event.coupon.domain.response.V1CouponErrorResponse;
import com.kakaopay.event.coupon.domain.response.V1ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class ErrorResponseConfig {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public V1ErrorMessage validator(ConstraintViolationException constraintViolationException) {
        String uuid = UUID.randomUUID().toString();
        log.error(String.format("VALIDATION_T_ID:%s", uuid), constraintViolationException);
        return new V1ErrorMessage(null, uuid);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CouponException.class)
    public V1CouponErrorResponse couponException(CouponException e) {
        return new V1CouponErrorResponse(e.getStatus().value);
    }

}
