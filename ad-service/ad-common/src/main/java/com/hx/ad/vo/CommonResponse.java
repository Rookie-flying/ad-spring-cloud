package com.hx.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//对response进行统一的封装
@Data //注在类上，提供类的get、set、equals、hashCode、canEqual、toString方法
@NoArgsConstructor//注在类上，提供类的无参构造
@AllArgsConstructor//注在类上，提供类的全参构造
public class CommonResponse<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    public CommonResponse(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
