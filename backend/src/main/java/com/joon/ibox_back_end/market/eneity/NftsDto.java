package com.joon.ibox_back_end.market.eneity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: backend
 * @description: 藏品信息实体类
 * @author: Joonnn
 * @create: 2025-03-2001:23
 **/
@Data
public class NftsDto {
    /**
     * 主键，唯一标识每个 NFT。
     */
    private Integer id;

    /**
     * NFT 的名称。
     */
    private String name;

    /**
     * NFT 的描述。
     */
    private String description;

    /**
     * NFT 图片的 URL。
     */
    private String imageUrl;

    /**
     * NFT 在链上的唯一标识符。
     */
    private String tokenId;

    /**
     * NFT 创建的时间戳。
     */
    private LocalDateTime createdAt;

    /**
     * NFT 信息最后更新的时间戳。
     */
    private LocalDateTime updatedAt;

    /**
     * 是否在市场上出售（布尔值）。
     */
    private Boolean isListed;

    /**
     * 创作者的版税百分比。
     */
    private BigDecimal royaltyPercentage;

    /**
     * 外键，指向 NFT 等级。
     */
    private Integer levelId;

    /**
     * 外键，指向 NFT 发行分数。
     */
    private Integer scoreId;
}
