package com.joon.ibox_back_end.common;

import com.joon.ibox_back_end.wallet.exception.BusinessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: backend
 * @description: 全局异常处理器
 * @author: Joonnn
 * @create: 2025-04-0200:10
 **/

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        return R.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        return R.error("系统繁忙，请稍后再试");
    }
}