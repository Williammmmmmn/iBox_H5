package com.joon.ibox_back_end.market.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: backend
 * @description: 寄售信息对应的实例的需要的信息
 * @author: Joonnn
 * @create: 2025-03-2400:36
 **/
@Data
public class NftInstanceDto {
    private int id; // 实例编号
    private int price; // 实例价格
    private String lockedBy; // 锁定者钱包地址
    private LocalDateTime lockedAt; // 锁定时间
    private LocalDateTime lockExpiry; // 锁定过期时间
}
