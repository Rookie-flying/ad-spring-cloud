package com.hx.ad.controller;

import com.alibaba.fastjson.JSON;
import com.hx.ad.exception.AdException;
import com.hx.ad.service.ICreativeService;
import com.hx.ad.vo.CreativeRequest;
import com.hx.ad.vo.CreativeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CreativeOPController {

    private final ICreativeService creativeService;


    @Autowired
    public CreativeOPController(ICreativeService creativeService) {
        this.creativeService = creativeService;
    }

    @PostMapping
    public CreativeResponse createCreative(
            @RequestBody CreativeRequest request) throws AdException {
        log.info("ad-sponsor: createCreative -> {}",
                JSON.toJSONString(request));
        return creativeService.createCreative(request);
    }
}
