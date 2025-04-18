package com.joon.ibox_back_end.market.entity.SellToRequest;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 用户售出藏品给求购者
 * @author: Joonnn
 * @create: 2025-04-1717:35
 **/
@Data
public class SellToRequest {
    private Integer instanceNumber;
    private String walletAddress;
    private BigDecimal price;
    private Integer nftId;
}
