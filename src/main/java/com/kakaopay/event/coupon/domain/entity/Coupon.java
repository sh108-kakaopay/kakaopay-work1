package com.kakaopay.event.coupon.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import javax.persistence.*;

import com.kakaopay.event.coupon.domain.enums.CouponStatus;
import lombok.*;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Table(name = "coupon")
@Entity
@Getter
@Setter
//@Builder
public class Coupon implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id", insertable = false, nullable = false)
    private Long couponId;

    @Column(name = "coupon", nullable = false, unique = true)
    private String coupon;

    @Column(name = "coupon_status", nullable = false)
    private CouponStatus status;

    @Column(name = "expired_timestamp", nullable = false)
    private LocalDateTime expiredTimestamp;

    @Column(name = "update_timestamp")
    private LocalDateTime updateTimestamp;

    @Column(name = "reg_timestamp", nullable = false)
    private LocalDateTime regTimestamp;


}