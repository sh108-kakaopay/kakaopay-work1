package com.kakaopay.event.coupon.controller.v1AuthController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kakaopay.event.coupon.repository.UserRepository;
import com.kakaopay.event.coupon.service.UserService;
import com.kakaopay.event.coupon.util.JwtUtil;
import com.kakaopay.event.coupon.util.MockMvcTestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@DisplayName("[로그인 API] [POST] /v1/auth/login TEST")
public class V1PostSignUpTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private final String url = "/v1/auth/signup";
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("[E] 이미 있는 유저로 요청하면 회원가입 실패")
    void 있는유저로_또_가입하면_회원가입_실패() throws Exception {
        String username = UUID.randomUUID().toString();
        userService.signup(username, username);
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(MockMvcTestUtil.buildUrlEncodedFormEntity(
                        "username", username,
                        "password", username
                ))
        ).andExpect(
                status().isInternalServerError()
        ).andDo(
                print()
        );
    }

    @Test
    @DisplayName("[S] 회원가입 성공")
    void 정상적으로_회원가입이_되는가() throws Exception {
        String username = "yangs";
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(MockMvcTestUtil.buildUrlEncodedFormEntity(
                        "username", username,
                        "password", username
                ))
        ).andExpect(
                status().isOk()
        ).andDo(
                print()
        );
    }

}