package com.hx.ad.dao;

import com.hx.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdUserRepository extends JpaRepository<AdUser, Long> {

    /**
     * 根据用户名查询用户记录*/
    AdUser findByUsername(String username);
}
