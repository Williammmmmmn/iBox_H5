package com.joon.ibox_back_end.common;

/**
 * @program: backend
 * @description: 常量类
 * @author: Joonnn
 * @create: 2025-04-1011:11
 **/

public class Constants {
    public static final int LOCK_DURATION_MINUTES = 3;

    // 定义错误常量
    public static final String ERROR_ALREADY_LOCKED = "该藏品已被其他用户锁定";
    public static final String ERROR_PENDING_ORDER = "当前存在待支付订单";
    public static final String ERROR_NFT_NOT_EXIST = "NFT实例不存在";
    public static final String ERROR_LOCK_FAILED = "已被其他用户锁定";
}
