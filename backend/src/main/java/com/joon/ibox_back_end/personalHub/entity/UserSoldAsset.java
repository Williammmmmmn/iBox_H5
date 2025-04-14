package com.joon.ibox_back_end.personalHub.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 用户已出售
 * @author: Joonnn
 * @create: 2025-04-1111:10
 **/
@Data
public class UserSoldAsset {
    private String name;        // 藏品名称
    private String imageUrl;    // 藏品图片
    private Integer instanceNumber; // 藏品编号
    private BigDecimal price;   // 出售价格
}
