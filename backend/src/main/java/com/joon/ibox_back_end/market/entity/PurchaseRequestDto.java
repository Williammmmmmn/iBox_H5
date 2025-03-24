package com.joon.ibox_back_end.market.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @program: backend
 * @description: 求购记录表DTO
 * @author: Joonnn
 * @create: 2025-03-2417:40
 **/
@Data
public class PurchaseRequestDto {
    private Integer id;
    private Integer nftId;
    private String buyerAddress;
    private BigDecimal price;
    private String currency;
    private String status;
    private LocalDateTime createdAt;
}
