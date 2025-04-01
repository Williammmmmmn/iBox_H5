package com.joon.ibox_back_end.commonEntity.po;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: backend
 * @description: 钱包po
 * @author: Joonnn
 * @create: 2025-04-0122:35
 **/
@Data
public class Wallet {
    /**
     * 钱包ID，主键
     */
    private Integer walletId;

    /**
     * 钱包地址，唯一，格式如0x...
     */
    private String walletAddress;

    /**
     * 关联用户ID
     */
    private Integer userId;

    /**
     * 钱包余额，支持小数
     */
    private BigDecimal balance;

    /**
     * 主要货币类型，默认ETH
     */
    private String currency;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 最后更新时间
     */
    private LocalDateTime updatedAt;
}
