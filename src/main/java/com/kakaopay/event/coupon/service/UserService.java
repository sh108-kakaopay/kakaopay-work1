package com.kakaopay.event.coupon.service;

import com.kakaopay.event.coupon.domain.entity.User;
import com.kakaopay.event.coupon.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User login(String username, String password) {
        User user = userRepository.findFirstByUsernameEquals(username);
        if (user == null) {
            return null;
        }
        if (passwordEncoder.matches(password, user.getPassword()) == false) {
            return null;
        }
        return user;
    }

    public User signup(String username, String password) {
        if (userRepository.findFirstByUsernameEquals(username) != null) {
            return null;
        }
        User signUpUser = new User();
        signUpUser.setUsername(username);
        signUpUser.setPassword(passwordEncoder.encode(password));
        return userRepository.saveAndFlush(signUpUser);
    }


//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findFirstByUsernameEquals(username);
//        if (user == null) {
//            throw new UsernameNotFoundException(String.format("User Not Found : %s", username));
//        }
//        return new LoginUserInfo(user.getUsername(), user.getPassword(), new ArrayList<>());
//    }
}
