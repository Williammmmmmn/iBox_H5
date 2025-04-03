package com.joon.ibox_back_end.personalHub.entity.cancelSale;

import lombok.Data;

import java.util.Date;

/**
 * @program: backend
 * @description: 取消寄售
 * @author: Joonnn
 * @create: 2025-04-0317:01
 **/

@Data
public class ConsignmentCancelSaleDto {
    private Long id;
    private Long instanceId;
    private String sellerAddress;
    private String status; // 'active', 'canceled'
    private Integer isActive; // 1 or 0
    private Date startTime;
    private Date endTime;
}
