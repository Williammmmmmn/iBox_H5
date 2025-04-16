package com.joon.ibox_back_end.market.entity.BuyDto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 用户购买其他用户寄售dto类
 * @author: Joonnn
 * @create: 2025-04-0801:04
 **/
@Data
public class BuyRequest {
    private Integer instanceId;
    private String walletAddress;
    private BigDecimal price;
}
