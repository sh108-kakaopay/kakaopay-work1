package com.kakaopay.event.coupon.repository;

import com.kakaopay.event.coupon.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findFirstByUsernameEquals(String username);
}