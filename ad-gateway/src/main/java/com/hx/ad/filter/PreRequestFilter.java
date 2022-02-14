package com.hx.ad.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PreRequestFilter extends ZuulFilter {

//  四种类型：Pre, router, post, error
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }
//  定义多个filter之间的执行优先级，数字越小，优先级越高
    @Override
    public int filterOrder() {
        return 0;
    }
//  是否执行该过滤器
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
//        获取本次请求的上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("startTime", System.currentTimeMillis());
        return null;
    }
}
