package com.kakaopay.event.coupon.controller.v1AuthController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kakaopay.event.coupon.domain.entity.User;
import com.kakaopay.event.coupon.domain.response.V1AuthTokenResponse;
import com.kakaopay.event.coupon.repository.UserRepository;
import com.kakaopay.event.coupon.service.UserService;
import com.kakaopay.event.coupon.util.AuthHeaderTestUtil;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@DisplayName("[로그인 API] [POST] /v1/auth/login TEST")
public class V1PostLoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthHeaderTestUtil authHeaderTestUtil;

    private final String url = "/v1/auth/login";
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
    @DisplayName("[E] 토큰이 시간이 지나면 잘 만료 되는가")
    void 토큰이_시간이_지나면_만료되는가() throws Exception {
        mockMvc.perform(
                get("/").header(authHeaderTestUtil.headerName(), authHeaderTestUtil.headerValue(LocalDateTime.now().minusHours(1)))
        ).andExpect(status().isUnauthorized()).andDo(print());
    }

    @Test
    @DisplayName("[E] 없는 유저로 요청 했을때, 로그인 실패")
    void 없는유저_로그인실패() throws Exception {
        String username = UUID.randomUUID().toString();
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(MockMvcTestUtil.buildUrlEncodedFormEntity(
                        "username", username,
                        "password", username
                ))
        ).andExpect(
                status().isUnauthorized()
        ).andDo(
                print()
        ).andReturn();
    }

    @Test
    @DisplayName("[S] 로그인 성공")
    void 정상적으로_로그인이_잘되는가() throws Exception {
        String username = "a";
        String password = "b";
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.saveAndFlush(user);

        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(MockMvcTestUtil.buildUrlEncodedFormEntity(
                        "username", username,
                        "password", password
                ))
        ).andExpect(
                status().isOk()
        ).andDo(
                print()
        ).andReturn();
        V1AuthTokenResponse v1AuthTokenResponse = objectMapper.readValue(result.getResponse().getContentAsString(), V1AuthTokenResponse.class);
        assertNotNull(v1AuthTokenResponse);
        DecodedJWT decodedJWT = jwtUtil.decode(v1AuthTokenResponse.getAccessToken());
        assertNotNull(decodedJWT);
        assertEquals(decodedJWT.getSubject(), username);
    }

}