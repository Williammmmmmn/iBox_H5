package com.joon.ibox_back_end.personalHub.entity.cancelSale;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 取消寄售
 * @author: Joonnn
 * @create: 2025-04-0317:02
 **/

@Data
public class NftInstanceCancelSaleDto {
    private Long instanceId;
    private Long nftId;
    private String ownerAddress;
    private Integer isConsigning; // 1 or 0
    private BigDecimal price;
}
