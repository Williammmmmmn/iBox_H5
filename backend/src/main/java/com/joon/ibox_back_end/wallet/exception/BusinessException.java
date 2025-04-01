package com.joon.ibox_back_end.wallet.exception;

/**
 * @program: backend
 * @description: 钱包业务异常类
 * @author: Joonnn
 * @create: 2025-04-0200:07
 **/

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
