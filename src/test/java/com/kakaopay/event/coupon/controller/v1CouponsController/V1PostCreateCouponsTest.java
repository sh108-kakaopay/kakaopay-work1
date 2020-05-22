package com.kakaopay.event.coupon.controller.v1CouponsController;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.event.coupon.domain.entity.Coupon;
import com.kakaopay.event.coupon.domain.enums.CouponStatus;
import com.kakaopay.event.coupon.repository.CouponRepository;
import com.kakaopay.event.coupon.util.MockMvcTestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@DisplayName("[쿠폰 생성 API] [POST] /v1/coupon TEST")
class V1PostCreateCouponsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponRepository couponRepository;

    private ObjectMapper objectMapper = new ObjectMapper();
    private final String url = "/v1/coupons";


    @AfterEach
    void tearDown() {
        couponRepository.deleteAll();
    }


    @Test
    @DisplayName("[S] 쿠폰 100개 넣기")
    void 쿠폰요청() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(1);

        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(MockMvcTestUtil.buildUrlEncodedFormEntity(
                        "coupon-size", "100",
                        "expired-datetime", localDateTime.toString()
                ))
        ).andExpect(
                status().isOk()
        ).andDo(
                print()
        ).andReturn();
        String[] coupons = objectMapper.readValue(result.getResponse().getContentAsString(), String[].class);
        assertEquals(coupons.length, 100);
        for (String couponSerial : coupons) {
            Coupon coupon = couponRepository.findFirstByCouponEquals(couponSerial);
            assertNotNull(coupon);
            assertEquals(coupon.getStatus(), CouponStatus.CREATE);
            assertEquals(coupon.getExpiredTimestamp(), localDateTime);
        }
    }


}
