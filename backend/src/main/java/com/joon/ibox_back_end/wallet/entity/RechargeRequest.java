package com.joon.ibox_back_end.wallet.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 充值请求
 * @author: Joonnn
 * @create: 2025-04-0200:19
 **/
@Data
public class RechargeRequest {
    private String walletAddress;
    private BigDecimal amount;

}
