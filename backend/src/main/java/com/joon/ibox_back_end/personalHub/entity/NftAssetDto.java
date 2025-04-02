package com.joon.ibox_back_end.personalHub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 藏品资产信息
 * @author: Joonnn
 * @create: 2025-04-0101:31
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NftAssetDto {
    private String nftName;
    private String imageUrl;
    private Integer instanceNumber;
    private Integer holdDays;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private Integer issueCount;
    private Integer instanceId;
    private Boolean isConsigning;


}
