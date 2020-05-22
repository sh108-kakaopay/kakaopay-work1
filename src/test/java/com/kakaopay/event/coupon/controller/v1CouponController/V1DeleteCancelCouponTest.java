package com.kakaopay.event.coupon.controller.v1CouponController;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kakaopay.event.coupon.domain.entity.Coupon;
import com.kakaopay.event.coupon.domain.enums.CouponErrorStatus;
import com.kakaopay.event.coupon.domain.enums.CouponStatus;
import com.kakaopay.event.coupon.domain.response.V1CouponErrorResponse;
import com.kakaopay.event.coupon.repository.CouponRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@DisplayName("[쿠폰 사용 취소 API][DELETE] /v1/coupon/{serial}/use")
class V1DeleteCancelCouponTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponRepository couponRepository;

    private ObjectMapper objectMapper = new ObjectMapper();
    private final String url = "/v1/coupon/%s/use";

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
                delete(String.format(url, UUID.randomUUID().toString()))
        )
                .andExpect(
                        status().isBadRequest()
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
        coupon.setRegTimestamp(LocalDateTime.MAX);

        couponRepository.saveAndFlush(coupon);

        MvcResult result = mockMvc.perform(
                delete(String.format(url, targetCouponSerial))
        )
                .andExpect(
                        status().isBadRequest()
                ).andDo(
                        print()
                ).andReturn();
        V1CouponErrorResponse v1CouponErrorResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), V1CouponErrorResponse.class);
        assertNotNull(v1CouponErrorResponse);
        assertEquals(v1CouponErrorResponse.getErrorCode(), CouponErrorStatus.EXPIRED.value);
    }

    @Test
    @DisplayName("[E] 사용 안된 쿠폰 취소 요청")
    void 사용안된_쿠폰_취소_요청() throws Exception {
        String targetCouponSerial = UUID.randomUUID().toString();
        LocalDateTime expired = LocalDateTime.now().plusHours(1);

        Coupon coupon = new Coupon();
        coupon.setCoupon(targetCouponSerial);

        coupon.setStatus(CouponStatus.ASSIGN);
        coupon.setExpiredTimestamp(expired);
        coupon.setRegTimestamp(LocalDateTime.MAX);
        couponRepository.saveAndFlush(coupon);

        MvcResult result = mockMvc.perform(
                delete(String.format(url, targetCouponSerial))
        )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        V1CouponErrorResponse v1CouponErrorResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), V1CouponErrorResponse.class);
        assertNotNull(v1CouponErrorResponse);
        assertEquals(v1CouponErrorResponse.getErrorCode(), CouponErrorStatus.NOT_ASSIGN.value);

    }


    @Test
    @DisplayName("[S] 사용된 쿠폰 취소 요청")
    void 사용된_쿠폰_취소_요청() throws Exception {
        String targetCouponSerial = UUID.randomUUID().toString();
        LocalDateTime expired = LocalDateTime.now().plusHours(1);

        Coupon coupon = new Coupon();
        coupon.setCoupon(targetCouponSerial);

        coupon.setStatus(CouponStatus.USE);
        coupon.setExpiredTimestamp(expired);
        coupon.setRegTimestamp(LocalDateTime.MAX);
        couponRepository.saveAndFlush(coupon);

        mockMvc.perform(delete(String.format(url, targetCouponSerial)))
                .andExpect(status().isOk())
                .andDo(print());
        assertEquals(couponRepository.findFirstByCouponEquals(targetCouponSerial).getStatus(), CouponStatus.ASSIGN);
    }


}
