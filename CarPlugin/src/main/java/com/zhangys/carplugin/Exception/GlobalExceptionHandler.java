package com.zhangys.carplugin.Exception;

import com.zhangys.carplugin.Entity.Result;
import com.zhangys.carplugin.Entity.StatusCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 捕获其他异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<String> handle(Exception e) {
        log.error("全局异常信息：{}", e.getMessage());
        return Result.fail(StatusCodeEnum.SC500.getCode(), StatusCodeEnum.SC500.getMsg() + "：" + e.getMessage());
    }

    public class FileNotExistException extends RuntimeException {
        public FileNotExistException(String message) {
            super(message);
        }
    }
}
