package com.joon.ibox_back_end.commonEntity.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

// OfficialOrder.java
@Data
public class OfficialOrder{
    private Integer id;
    private String orderId;
    private Integer nftId;
    private String userAddress;
    private Integer quantity;
    private BigDecimal price;
    private String status; // "pending", "locked", "completed", "canceled"
    private Date lockExpiry;
    private Date createdAt;
    private String transactionHash;
}

