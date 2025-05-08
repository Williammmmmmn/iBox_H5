package com.joon.ibox_back_end.saleRecord.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-05-0817:14
 **/
@Data
public class SaleRecordDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private String currency;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer issueCount;
    private Integer circulateCount;
    private String status; // "进行中"或"已结束"
    private String category; // "藏品"或"盲盒"
}
