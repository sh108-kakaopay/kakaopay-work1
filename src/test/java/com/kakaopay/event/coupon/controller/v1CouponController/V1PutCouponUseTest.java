package com.kakaopay.event.coupon.controller.v1CouponController;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kakaopay.event.coupon.domain.entity.Coupon;
import com.kakaopay.event.coupon.domain.enums.CouponErrorStatus;
import com.kakaopay.event.coupon.domain.enums.CouponStatus;
import com.kakaopay.event.coupon.domain.response.V1CouponErrorResponse;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@DisplayName("[쿠폰 사용 API] [PUT] /v1/coupon/{serial}/use TEST")
class V1PutCouponUseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private AuthHeaderTestUtil authHeaderTestUtil;

    private final String url = "/v1/coupon/%s/use";
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void tearDown() {
        couponRepository.deleteAll();
    }

    @Test
    @DisplayName("[E] 없는 쿠폰을 요청 했을때 에러")
    void 없는_쿠폰을_요청_했을때_에러() throws Exception {
        MvcResult result = mockMvc.perform(
                put(String.format(url, UUID.randomUUID().toString()))
                        .header(authHeaderTestUtil.headerName(), authHeaderTestUtil.headerValue())
        ).andExpect(
                status().isNotAcceptable()
        ).andDo(
                print()
        ).andReturn();

        V1CouponErrorResponse v1CouponErrorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), V1CouponErrorResponse.class);
        assertNotNull(v1CouponErrorResponse);
        assertEquals(v1CouponErrorResponse.getErrorCode(), CouponErrorStatus.NOT_FOUND.value);
    }

    @Test
    @DisplayName("[E] 만료된 쿠폰을 요청 했을때 에러")
    void 만료된_쿠폰을_요청_했을때_에러() throws Exception {
        String targetCouponSerial = UUID.randomUUID().toString();

        Coupon coupon = new Coupon();
        coupon.setCoupon(targetCouponSerial);

        coupon.setStatus(CouponStatus.CREATE);
        coupon.setExpiredTimestamp(LocalDateTime.now());
        coupon.setRegTimestamp(LocalDateTime.now());

        couponRepository.saveAndFlush(coupon);

        MvcResult result = mockMvc.perform(
                put(String.format(url, targetCouponSerial))
                        .header(authHeaderTestUtil.headerName(), authHeaderTestUtil.headerValue())
        ).andExpect(
                status().isNotAcceptable()
        ).andDo(
                print()
        ).andReturn();
        V1CouponErrorResponse v1CouponErrorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), V1CouponErrorResponse.class);
        assertNotNull(v1CouponErrorResponse);
        assertEquals(v1CouponErrorResponse.getErrorCode(), CouponErrorStatus.EXPIRED.value);
    }

    //
    @Test
    @DisplayName("[E] 36자리에 맞춰서 요청하지 않았을경우")
    void 시리얼을_36자리에_맞춰서_요청하지_않았을경우() throws Exception {
        mockMvc.perform(
                put(String.format(url, UUID.randomUUID().toString() + "1"))
                        .header(authHeaderTestUtil.headerName(), authHeaderTestUtil.headerValue())
        ).andExpect(
                status().isBadRequest()
        ).andDo(
                print()
        );
    }


    @Test
    @DisplayName("[S] 쿠폰 사용")
    void 쿠폰요청() throws Exception {
        String targetCouponSerial = UUID.randomUUID().toString();
        LocalDateTime expired = LocalDateTime.now().plusHours(1);

        Coupon coupon = new Coupon();
        coupon.setCoupon(targetCouponSerial);

        coupon.setStatus(CouponStatus.ASSIGN);
        coupon.setExpiredTimestamp(expired);
        coupon.setRegTimestamp(LocalDateTime.now());
        couponRepository.saveAndFlush(coupon);

        mockMvc.perform(
                put(String.format(url, targetCouponSerial))
                        .header(authHeaderTestUtil.headerName(), authHeaderTestUtil.headerValue())
        ).andExpect(
                status().isOk()
        ).andDo(
                print()
        );

        assertEquals(couponRepository.findFirstByCouponEquals(targetCouponSerial).getStatus(), CouponStatus.USE);
    }


}
