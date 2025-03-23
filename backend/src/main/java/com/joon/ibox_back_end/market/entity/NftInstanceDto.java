package com.joon.ibox_back_end.market.entity;

import lombok.Data;

/**
 * @program: backend
 * @description: 寄售信息对应的实例的需要的信息
 * @author: Joonnn
 * @create: 2025-03-2400:36
 **/
@Data
public class NftInstanceDto {
    private int id; // 实例编号
    private String price; // 实例价格
}
