package com.hx.ad.dao;

import com.hx.ad.entity.Creative;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreativeRepository extends JpaRepository<Creative, Long> {

    //不写，会使用JpaRepository默认的方法
}
