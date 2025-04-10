package com.joon.ibox_back_end.commonEntity.po;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @program: backend
 * @description: 藏品实例信息
 * @author: Joonnn
 * @create: 2025-03-2001:39
 **/
@Data
public class Instances {
    /**
     * 主键，唯一标识每个 NFT 实例。
     */
    private Integer instanceId;

    /**
     * 外键，指向 NFT 模板。
     */
    private Integer nftId;

    /**
     * 当前拥有者的钱包地址。
     */
    private String ownerAddress;

    /**
     * 实例创建的时间戳。
     */
    private LocalDateTime createdAt;

    /**
     * 实例信息最后更新的时间戳。
     */
    private LocalDateTime updatedAt;
    /**
     * 示例编号
     */
    private Integer instanceNumber;
    /**
     * 此藏品实例的寄售价格（如果用户寄售了）。
     */
    private BigDecimal price;
    /**
     * 是否正在寄售
     */
    private Boolean isConsigning;
    /**
     * 版本号
     */
    private Integer version; // 新增的乐观锁版本字段
    /**
     * 锁定者钱包地址
     */
    private String lockedBy;
    /**
     * 锁定开始的时间
     */
    private LocalDateTime lockedAt;
    /**
     * 锁定期限
     */
    private LocalDateTime lockExpiry;
}
