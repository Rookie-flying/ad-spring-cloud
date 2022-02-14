package com.hx.ad.service;

import com.hx.ad.exception.AdException;
import com.hx.ad.vo.CreativeRequest;
import com.hx.ad.vo.CreativeResponse;

public interface ICreativeService {

    CreativeResponse createCreative(CreativeRequest request) throws AdException;
}
