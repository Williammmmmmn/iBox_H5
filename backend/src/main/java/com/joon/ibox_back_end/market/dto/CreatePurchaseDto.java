package com.joon.ibox_back_end.market.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 发起求购dto
 * @author: Joonnn
 * @create: 2025-04-1918:16
 **/
@Data
public class CreatePurchaseDto {
    private Integer nftId;
    private String buyerAddress;
    private BigDecimal price;
    private Integer quantity;
}
