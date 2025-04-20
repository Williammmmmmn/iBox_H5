package com.joon.ibox_back_end.wallet.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: backend
 * @description: 交易记录Dto实体类
 * @author: Joonnn
 * @create: 2025-04-2023:24
 **/
@Data
public class TransactionRecordDto {
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
     * 交易类型（如购买、出售、转账、寄售、求购等,还有求购表中的state，发起和退款）。
     */
    private String type;

    /**
     * 外键，指向求购记录（如果是求购交易）。
     */
    private Integer purchaseRequestId;


    /**
     * 外键，指向的 nft实例ID。
     */
    private Integer instanceId;
}
