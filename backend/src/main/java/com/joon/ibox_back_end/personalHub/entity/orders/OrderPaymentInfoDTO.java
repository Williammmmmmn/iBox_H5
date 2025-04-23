package com.joon.ibox_back_end.personalHub.entity.orders;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: backend
 * @description: 待支付订单
 * @author: Joonnn
 * @create: 2025-04-2314:40
 **/

@Data
public class OrderPaymentInfoDTO {
    private Integer nftId;
    private Integer instanceNumber;
    private LocalDateTime lockedAt;
    private LocalDateTime lockExpiry;
    private Integer remainingSeconds;
}
