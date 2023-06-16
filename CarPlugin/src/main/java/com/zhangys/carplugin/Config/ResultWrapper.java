package com.zhangys.carplugin.Config;

import com.alibaba.fastjson.JSON;
import com.zhangys.carplugin.Entity.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = "com.zhangys.carplugin.Controller")
public class ResultWrapper implements ResponseBodyAdvice<Object> {

    /**
     * 是否支持advice功能
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * 对返回的数据进行处理
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof String) {
            return JSON.toJSONString(Result.success(o));
        }
        // 这个判断的作用：防止全局异常处理后返回的结果（类型为Result）再次被包装
        if (o instanceof Result) {
            return o;
        }
        return Result.success(o);
    }

}
