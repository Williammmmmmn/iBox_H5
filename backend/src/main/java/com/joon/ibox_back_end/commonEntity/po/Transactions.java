package com.joon.ibox_back_end.commonEntity.po;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: backend
 * @description: 交易记录
 * @author: Joonnn
 * @create: 2025-03-2001:28
 **/
@Data
public class Transactions {
    /**
     * 主键，唯一标识每笔交易。
     */
    private Integer id;

    /**
     * 外键，指向交易的 NFT。
     */
    private Integer nftId;

    /**
     * 交易发起方的钱包地址。
     */
    private String fromAddress;

    /**
     * 交易接收方的钱包地址。
     */
    private String toAddress;

    /**
     * 区块链上的交易哈希。
     */
    private String transactionHash;

    /**
     * 交易价格。
     */
    private BigDecimal price;

    /**
     * 交易使用的货币类型。
     */
    private String currency;

    /**
     * 交易发生的时间戳。
     */
    private LocalDateTime transactionDate;

    /**
     * 交易类型（如购买、出售、转账、寄售、求购等）。
     */
    private TransactionType type;

    /**
     * 外键，指向求购记录（如果是求购交易）。
     */
    private Integer purchaseRequestId;

    /**
     * 交易类型枚举。
     */
    public enum TransactionType {
        PURCHASE, SALE, TRANSFER, CONSIGNMENT, PURCHASE_REQUEST
    }
    /**
     * 外键，指向的 nft实例ID。
     */
    private Integer instanceId;
}
