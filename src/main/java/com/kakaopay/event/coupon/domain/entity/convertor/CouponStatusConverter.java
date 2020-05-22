package com.kakaopay.event.coupon.domain.entity.convertor;

import com.kakaopay.event.coupon.domain.enums.CouponStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class CouponStatusConverter implements AttributeConverter<CouponStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CouponStatus couponStatus) {
        return couponStatus.value;
    }

    @Override
    public CouponStatus convertToEntityAttribute(Integer integer) {
        return Stream.of(CouponStatus.values())
                .filter(c -> c.value == integer)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
