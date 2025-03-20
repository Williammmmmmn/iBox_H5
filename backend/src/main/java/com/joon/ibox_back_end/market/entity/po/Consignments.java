package com.joon.ibox_back_end.market.entity.po;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: backend
 * @description: 存储寄售信息
 * @author: Joonnn
 * @create: 2025-03-2001:32
 **/
@Data
public class Consignments {
    /**
     * 主键，唯一标识每个寄售记录。
     */
    private Integer id;

    /**
     * 外键，指向 NFT 实例。
     */
    private Integer instanceId;

    /**
     * 寄售者的钱包地址。
     */
    private String sellerAddress;

    /**
     * 寄售最高价（限价，可选）。
     */
    private BigDecimal maxPrice;

    /**
     * 价格的货币类型（如 ETH、USD 等）。
     */
    private String currency;

    /**
     * 寄售开始时间。
     */
    private LocalDateTime startTime;

    /**
     * 寄售结束时间（可选）。
     */
    private LocalDateTime endTime;

    /**
     * 寄售状态（进行中、已完成、已取消）。
     */
    private ConsignmentStatus status;

    /**
     * 寄售创建的时间戳。
     */
    private LocalDateTime createdAt;

    /**
     * 寄售信息最后更新的时间戳。
     */
    private LocalDateTime updatedAt;

    /**
     * 寄售状态枚举。
     */
    public enum ConsignmentStatus {
        ACTIVE("active"),
        COMPLETED("completed"),
        CANCELED("canceled");

        private final String status;

        ConsignmentStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}
