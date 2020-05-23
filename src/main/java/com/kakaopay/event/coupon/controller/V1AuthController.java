package com.kakaopay.event.coupon.controller;

import com.kakaopay.event.coupon.domain.entity.User;
import com.kakaopay.event.coupon.domain.exception.InternalServerErrorException;
import com.kakaopay.event.coupon.domain.exception.UnauthorizedException;
import com.kakaopay.event.coupon.domain.response.V1AuthTokenResponse;
import com.kakaopay.event.coupon.service.UserService;
import com.kakaopay.event.coupon.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class V1AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public V1AuthTokenResponse login(@RequestParam("username") String username,
                                     @RequestParam("password") String password) throws UnauthorizedException {
        User user = userService.login(username, password);
        if (user == null) {
            throw new UnauthorizedException();
        }
        LocalDateTime expiredTimestamp = jwtUtil.buildExpiredTimestamp();
        String token = jwtUtil.encode(user.getUsername(), expiredTimestamp);

        return new V1AuthTokenResponse(token, expiredTimestamp);
    }

    @PostMapping("/signup")
    public void signup(@RequestParam("username") String username,
                       @RequestParam("password") String password) {
        User user = userService.signup(username, password);
        if (user == null || user.getUserId() == null) {
            throw new InternalServerErrorException();
        }
    }
}
