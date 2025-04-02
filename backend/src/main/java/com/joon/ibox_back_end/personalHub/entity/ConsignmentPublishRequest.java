package com.joon.ibox_back_end.personalHub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 寄售发布请求体dto
 * @author: Joonnn
 * @create: 2025-04-0217:11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsignmentPublishRequest {
    Integer instanceId;
    String sellerAddress;
    BigDecimal price;
    String currency;
}
