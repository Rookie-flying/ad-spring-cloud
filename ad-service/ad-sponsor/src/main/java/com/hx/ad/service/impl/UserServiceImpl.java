package com.hx.ad.service.impl;

import com.hx.ad.constant.Constants;
import com.hx.ad.dao.AdUserRepository;
import com.hx.ad.entity.AdUser;
import com.hx.ad.exception.AdException;
import com.hx.ad.service.IUserService;
import com.hx.ad.util.CommonUtils;
import com.hx.ad.vo.CreateUserRequest;
import com.hx.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final AdUserRepository userRepository;

    @Autowired
    public UserServiceImpl(AdUserRepository userRepository){

        this.userRepository = userRepository;
    }

    @Override
    @Transactional //由于要写入，需要开启事务
    public CreateUserResponse createUser(CreateUserRequest request)
            throws AdException {

        if(!request.validate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdUser oldUser = userRepository.
                findByUsername(request.getUsername());
        if (oldUser != null) {
            throw new AdException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }
        AdUser newUser = userRepository.save(new AdUser(
                request.getUsername(),
                CommonUtils.md5(request.getUsername())));
        return new CreateUserResponse(
                newUser.getId(), newUser.getUsername(), newUser.getToken(),
                newUser.getCreateTime(), newUser.getUpdateTime()
        );
    }
}
