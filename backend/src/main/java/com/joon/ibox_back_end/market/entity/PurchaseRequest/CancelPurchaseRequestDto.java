package com.joon.ibox_back_end.market.entity.PurchaseRequest;

import lombok.Data;

/**
 * @program: backend
 * @description: 取消求购实体类Dto
 * @author: Joonnn
 * @create: 2025-04-2715:57
 **/
@Data
public class CancelPurchaseRequestDto {
    private String walletAddress;
    private Integer purchaseId;
}
