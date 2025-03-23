package com.joon.ibox_back_end.market.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: backend
 * @description: 具体藏品寄售返回信息
 * @author: Joonnn
 * @create: 2025-03-2400:30
 **/
@Data
public class NftSaleDto {
    private Integer nftId; // NFT ID
    private String name; // NFT 名称
    private String imageUrl; // NFT 图片链接
    private int issueCount; // 发行量
    private int circulationCount; // 流通量
    private List<NftInstanceDto> instances; // NFT 实例列表
}
