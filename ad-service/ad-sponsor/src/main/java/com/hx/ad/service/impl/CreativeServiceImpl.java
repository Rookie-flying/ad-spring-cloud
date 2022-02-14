package com.hx.ad.service.impl;

import com.hx.ad.dao.CreativeRepository;
import com.hx.ad.entity.Creative;
import com.hx.ad.exception.AdException;
import com.hx.ad.service.ICreativeService;
import com.hx.ad.vo.CreativeRequest;
import com.hx.ad.vo.CreativeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreativeServiceImpl implements ICreativeService {

    private final CreativeRepository creativeRepository;

    @Autowired
    public CreativeServiceImpl(CreativeRepository creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

    @Override
    public CreativeResponse createCreative(CreativeRequest request) throws AdException {

        Creative creative = creativeRepository.save(
                request.convertToEntity()
        );
        return new CreativeResponse(creative.getId(), creative.getName());
    }
}
