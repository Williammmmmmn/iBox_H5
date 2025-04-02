package com.joon.ibox_back_end.personalHub.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: backend
 * @description: 寄售发布Dto
 * @author: Joonnn
 * @create: 2025-04-0216:36
 **/

@Data
public class ConsignmentPublishDto {
    private Integer id;
    private Integer instanceId;
    private String sellerAddress;
    private String currency;
    private BigDecimal price;
    private Date startTime;
    private Date endTime;
    private String status; // 'active','completed','canceled'
    private Boolean isActive;
}
