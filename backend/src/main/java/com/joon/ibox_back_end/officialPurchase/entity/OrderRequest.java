package com.joon.ibox_back_end.officialPurchase.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-06-0915:27
 **/
@Data
public class OrderRequest {
    private Integer nftId;
    private String userAddress;
    private Integer quantity;
    private BigDecimal price;
}
