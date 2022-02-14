package com.hx.ad.advice;

import com.hx.ad.annotation.IgnoreResponseAdvice;
import com.hx.ad.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {
    // 响应是否支持拦截
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 类上注解了 ignoreResponseAdvice, 则不进行处理
        if (returnType.getDeclaringClass().isAnnotationPresent(
                IgnoreResponseAdvice.class
        )) {
           return false;
        }
        // 方法上注解了 ignoreResponseAdvice，则不进行处理
        if (returnType.getMethod().isAnnotationPresent(
                IgnoreResponseAdvice.class
        )) {
            return false;
        }
        return true;
    }
    // 根据方法的名字可以知道，这个方法实现了在结果输出前的操作。
    // 这个方法的参数很多，我们只需要关注第一个：Object，这个就是原始的 Controller 返回的内容。
    // 我们也就是需要对它进行包装。
    @Nullable
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(@Nullable Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // 定义最终的返回对象
        CommonResponse<Object> response_1 = new CommonResponse<>(0,"");

        // 如果 body 是 null, 则response不需要设置 data
        if (body == null){
            return response_1;
        // 如果 o 已经是 CommonResponse 类型, 强转即可
        } else if (body instanceof CommonResponse) {
            response_1 = (CommonResponse<Object>) body;
        // 否则, 把响应对象作为 CommonResponse 的 data 部分
        } else {
            response_1.setData(body);
        }

        return response_1;
    }
}
