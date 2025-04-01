package com.joon.ibox_back_end.wallet.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 提现
 * @author: Joonnn
 * @create: 2025-04-0200:19
 **/
@Data
public class WithdrawRequest {
    private String walletAddress;
    private BigDecimal amount;
}
