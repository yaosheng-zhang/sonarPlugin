package com.zhangys.carplugin.Entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data) {
        return Result.success(StatusCodeEnum.SC200.getMsg(), data);
    }

    public static <T> Result<T> success(String msg, T data) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(StatusCodeEnum.SC200.getCode());
        apiResult.setMsg(msg);
        apiResult.setData(data);
        return apiResult;
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
