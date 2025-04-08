package com.joon.ibox_back_end.market.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 寄售详情dto
 * @author: Joonnn
 * @create: 2025-03-2617:51
 **/
@Data
public class NftSaleDetaiDto {
    private Integer nftId;
    private String nftName;
    private String nftDescription;
    private String nftImage;
    private Integer issueCount;
    private Integer circulationCount;
    private Integer instanceNumber;
    private BigDecimal instancePrice;
    private Integer instanceId;
}
