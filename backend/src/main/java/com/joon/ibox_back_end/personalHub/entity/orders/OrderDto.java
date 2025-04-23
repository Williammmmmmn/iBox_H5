package com.joon.ibox_back_end.personalHub.entity.orders;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: backend
 * @description: 订单Dto
 * @author: Joonnn
 * @create: 2025-04-2210:35
 **/
@Data
public class OrderDto {
    private String orderId;       // 订单ID
    private String status;       // 订单状态 (待支付, 求购中, 已完成)
    private String imageUrl;     // NFT图片URL
    private String name;         // NFT名称
    private LocalDateTime createdAt;      // 订单创建时间
    private String type;         // 订单类型
    private BigDecimal price;    // 订单价格
    private String walletAddress; // 关联的钱包地址
}
