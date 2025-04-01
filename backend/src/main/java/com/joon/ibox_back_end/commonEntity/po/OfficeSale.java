package com.joon.ibox_back_end.commonEntity.po;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: backend
 * @description: 官方销售信息
 * @author: Joonnn
 * @create: 2025-03-2001:27
 **/
@Data
public class OfficeSale {
    /**
     * 主键，唯一标识每个官方销售记录。
     */
    private Integer id;

    /**
     * 外键，指向 NFT。
     */
    private Integer nftId;

    /**
     * 官方销售价格。
     */
    private BigDecimal price;

    /**
     * 价格的货币类型（如 ETH、USD 等）。
     */
    private String currency;

    /**
     * 销售开始时间。
     */
    private LocalDateTime startTime;

    /**
     * 销售结束时间。
     */
    private LocalDateTime endTime;

    /**
     * 销售记录创建的时间戳。
     */
    private LocalDateTime createdAt;

    /**
     * 销售记录最后更新的时间戳。
     */
    private LocalDateTime updatedAt;

    /**
     * 流通份数，也就是官方卖出去的份数。
     */
    private Integer circulateCount;
    /**
     * 发行份数。
     */
    private Integer issueCount;
}
