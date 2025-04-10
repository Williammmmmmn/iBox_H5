package com.joon.ibox_back_end.market.entity;

import lombok.Data;

/**
 * @program: backend
 * @description: 锁定订单请求
 * @author: Joonnn
 * @create: 2025-04-1010:53
 **/
@Data
public class LockRequest {
    private String walletAddress;
    private Integer instanceId;
}
