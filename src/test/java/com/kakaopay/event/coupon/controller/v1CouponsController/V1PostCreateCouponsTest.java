package com.kakaopay.event.coupon.controller.v1CouponsController;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.event.coupon.domain.entity.Coupon;
import com.kakaopay.event.coupon.domain.enums.CouponStatus;
import com.kakaopay.event.coupon.repository.CouponRepository;
import com.kakaopay.event.coupon.util.AuthHeaderTestUtil;
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

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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

    @Autowired
    private AuthHeaderTestUtil authHeaderTestUtil;
    private final String url = "/v1/coupons";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void tearDown() {
        couponRepository.deleteAll();
    }


    @Test
    @DisplayName("[E] 쿠폰 1000개 이상 넣으면 Bad Request")
    void 쿠폰이_만개이상이라면_BAD_REQUEST() throws Exception {
        mockMvc.perform(post(url)
                .header(authHeaderTestUtil.headerName(), authHeaderTestUtil.headerValue())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(MockMvcTestUtil.buildUrlEncodedFormEntity(
                        "coupon-size", "1001",
                        "expired-datetime", ZonedDateTime.now().toString()
                ))
        ).andExpect(
                status().isBadRequest()
        ).andDo(
                print()
        );
    }


    @Test
    @DisplayName("[S] 쿠폰 100개 넣기")
    void 쿠폰요청() throws Exception {
        int size = 1000;
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        MvcResult result = mockMvc.perform(post(url)
                .header(authHeaderTestUtil.headerName(), authHeaderTestUtil.headerValue())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(MockMvcTestUtil.buildUrlEncodedFormEntity(
                        "coupon-size", size + "",
                        "expired-datetime", zonedDateTime.toString()
                ))
        ).andExpect(
                status().isOk()
        ).andDo(
                print()
        ).andReturn();
        String[] coupons = objectMapper.readValue(result.getResponse().getContentAsString(), String[].class);
        assertEquals(coupons.length, size);
        for (String couponSerial : coupons) {
            Coupon coupon = couponRepository.findFirstByCouponEquals(couponSerial);
            assertNotNull(coupon);
            assertEquals(coupon.getStatus(), CouponStatus.CREATE);
            assertEquals(coupon.getExpiredTimestamp().toEpochSecond(ZoneOffset.UTC), zonedDateTime.toLocalDateTime().toEpochSecond(ZoneOffset.UTC));
        }
    }


}
