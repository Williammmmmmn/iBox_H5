package com.joon.ibox_back_end.market.entity.po;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: backend
 * @description: 求购记录表映射
 * @author: Joonnn
 * @create: 2025-03-2417:34
 **/
@Data
public class purchaseRequests {
    /**
     * 主键，唯一标识每个求购记录
     */
    private Integer id;

    /**
     * 外键，指向被求购的NFT
     */
    private Integer nftId;

    /**
     * 求购者的钱包地址
     */
    private String buyerAddress;

    /**
     * 求购价格
     */
    private BigDecimal price;

    /**
     * 价格的货币类型（如ETH、USD等）
     */
    private String currency;

    /**
     * 求购状态（全部、求购中、已完成、已退款、已取消）
     */
    private String status;

    /**
     * 求购创建的时间戳
     */
    private LocalDateTime createdAt;

    /**
     * 求购信息最后更新的时间戳
     */
    private LocalDateTime updatedAt;

    /**
     * 外键，指向NFT实例
     */
    private Integer instanceId;
}
