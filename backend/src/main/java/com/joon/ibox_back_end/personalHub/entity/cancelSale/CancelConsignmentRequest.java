package com.joon.ibox_back_end.personalHub.entity.cancelSale;

import com.sun.istack.internal.NotNull;
import lombok.Data;

/**
 * @program: backend
 * @description: 请求体
 * @author: Joonnn
 * @create: 2025-04-0317:16
 **/
@Data
public class CancelConsignmentRequest {
    private String walletAddress;
    private Integer instanceId;
}
