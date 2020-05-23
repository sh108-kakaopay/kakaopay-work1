package com.kakaopay.event.coupon.controller.v1CouponsController;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kakaopay.event.coupon.domain.entity.Coupon;
import com.kakaopay.event.coupon.domain.enums.CouponStatus;
import com.kakaopay.event.coupon.domain.response.V1CouponResultResponse;
import com.kakaopay.event.coupon.repository.CouponRepository;
import com.kakaopay.event.coupon.util.AuthHeaderTestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@DisplayName("[만료 쿠폰 호출 API] [GET] /v1/coupons/expired TEST")
class V1GetExpiredCouponsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private AuthHeaderTestUtil authHeaderTestUtil;

    private ObjectMapper objectMapper = new ObjectMapper();
    private final String url = "/v1/coupons/expired";

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void tearDown() {
        couponRepository.deleteAll();
    }


    @Test
    @DisplayName("[S] 디비에 아무것도 없을때도 빈 List Return")
    void 디비에_아무것도_없을때도_빈값() throws Exception {
        MvcResult result = mockMvc.perform(
                get(url).header(authHeaderTestUtil.headerName(), authHeaderTestUtil.headerValue())

        ).andExpect(
                status().isOk()
        ).andDo(
                print()
        ).andReturn();
        V1CouponResultResponse[] v1CouponErrorResponses = objectMapper.readValue(result.getResponse().getContentAsString(), V1CouponResultResponse[].class);
        assertEquals(v1CouponErrorResponses.length, 0);
    }


    @Test
    @DisplayName("[S] 만료된 쿠폰이 없을 경우 빈 List Return")
    void 만료된_쿠폰이_없는경우_빈LIST_반환() throws Exception {
        String targetCouponSerial = UUID.randomUUID().toString();

        Coupon coupon = new Coupon();
        coupon.setCoupon(targetCouponSerial);

        coupon.setStatus(CouponStatus.CREATE);
        coupon.setExpiredTimestamp(LocalDateTime.now().plusHours(1));
        coupon.setRegTimestamp(LocalDateTime.MAX);

        couponRepository.saveAndFlush(coupon);

        MvcResult result = mockMvc.perform(
                get(url).header(authHeaderTestUtil.headerName(), authHeaderTestUtil.headerValue())
        ).andExpect(
                status().isOk()
        ).andDo(
                print()
        ).andReturn();
        V1CouponResultResponse[] v1CouponErrorResponses = objectMapper.readValue(result.getResponse().getContentAsString(), V1CouponResultResponse[].class);
        assertEquals(v1CouponErrorResponses.length, 0);
    }


    @Test
    @DisplayName("[S] 만료된 쿠폰 요청 ")
    void 만료된_쿠폰_요청() throws Exception {
        String targetCouponSerial = UUID.randomUUID().toString();
        Coupon coupon = new Coupon();
        coupon.setCoupon(targetCouponSerial);

        coupon.setStatus(CouponStatus.ASSIGN);
        coupon.setExpiredTimestamp(LocalDateTime.now());
        coupon.setRegTimestamp(LocalDateTime.MAX);
        couponRepository.saveAndFlush(coupon);

        MvcResult result = mockMvc.perform(
                get(url).header(authHeaderTestUtil.headerName(), authHeaderTestUtil.headerValue())
        ).andExpect(
                status().isOk()
        ).andDo(
                print()
        ).andReturn();
        V1CouponResultResponse[] v1CouponErrorResponses = objectMapper.readValue(result.getResponse().getContentAsString(), V1CouponResultResponse[].class);
        assertEquals(v1CouponErrorResponses.length, 1);
        assertEquals(v1CouponErrorResponses[0].getSerial(), targetCouponSerial);
    }
}
