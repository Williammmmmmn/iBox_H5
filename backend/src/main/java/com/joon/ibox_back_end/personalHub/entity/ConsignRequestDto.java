package com.joon.ibox_back_end.personalHub.entity;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 更改状态dto->"未寄售"->"寄售"
 * @author: Joonnn
 * @create: 2025-04-0100:43
 **/
@Data
public class ConsignRequestDto {
    @NotNull
    private Integer instanceId;

    private BigDecimal price;
}
