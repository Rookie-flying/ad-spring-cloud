package com.hx.ad.service;

import com.hx.ad.exception.AdException;
import com.hx.ad.vo.CreateUserRequest;
import com.hx.ad.vo.CreateUserResponse;

public interface IUserService {

//    创建用户
    CreateUserResponse createUser(CreateUserRequest request)
        throws AdException;
}
