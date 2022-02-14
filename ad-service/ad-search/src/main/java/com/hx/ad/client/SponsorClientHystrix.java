package com.hx.ad.client;

import com.hx.ad.client.vo.AdPlan;
import com.hx.ad.client.vo.AdPlanGetRequest;
import com.hx.ad.vo.CommonResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SponsorClientHystrix implements SponsorClient{


    @Override
    public CommonResponse<List<AdPlan>> getAdPlans(
            AdPlanGetRequest request) {
        return new CommonResponse<>(-1, "eureka-client-ad-sponsor error");
    }
}
