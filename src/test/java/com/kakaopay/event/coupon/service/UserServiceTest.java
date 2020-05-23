package com.kakaopay.event.coupon.service;

import com.kakaopay.event.coupon.domain.entity.User;
import com.kakaopay.event.coupon.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("[S] 로그인이 잘 되는가")
    void login() {
        String username = "a";
        String password = "b";
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.saveAndFlush(user);
        User login = userService.login(username, password);
        assertNotNull(login);
        assertEquals(login.getUsername(), username);
    }

    @Test
    @DisplayName("[E] 없는 사용자로 로그인 시도시 로그인 불가")
    void 없는사용자로_로그인시_실패() {
        User login = userService.login("a", "b");
        assertNull(login);
    }


    @Test
    @DisplayName("[E] 비밀번호가 틀린 상태로 로그인 시도시 로그인 불가")
    void 비밀번호가_틀린_상태로_로그인시_실패() {
        String username = "a";
        String password = "b";
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.saveAndFlush(user);
        User login = userService.login(username, password + "1");
        assertNull(login);
    }

    @Test
    @DisplayName("[S] 회원가입이 잘 되는가")
    void signup() {
        String username = "a";
        String password = "b";

        User sign = userService.signup(username, password);
        assertNotNull(sign);
        User findUser = userRepository.findFirstByUsernameEquals(username);
        assertNotNull(findUser);
        assertEquals(findUser.getUsername(), sign.getUsername());
        assertEquals(findUser.getPassword(), sign.getPassword());
        assertEquals(findUser.getUserId(), sign.getUserId());
    }


    @Test
    @DisplayName("[S] 회원가입때 입력한 아이디/비밀번호로 잘 로그인이 되는가")
    void 회원가입했을때_입력한_비밀번호로_로그인이_되는가() {
        String username = "a";
        String password = "b";

        User sign = userService.signup(username, password);
        assertNotNull(sign);
        assertNotNull(userService.login(username, password));
    }
}