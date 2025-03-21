package com.joon.ibox_back_end.market.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 用于映射返回市场列表展示
 * @author: Joonnn
 * @create: 2025-03-2016:47
 **/
@Data
public class NftListDto {
    private String nftId;
    private String name; // NFT 名称
    private String imageUrl; // NFT 图片 URL
    private String levelName; //NFT等级
    private Integer totalCirculateCount; // 总流通份数
    private Integer totalIssueCount; // 总发行份数
    private BigDecimal lowestPrice; // 最低寄售价格
    private Integer dailyTransactionCount; // 当天交易量
}
