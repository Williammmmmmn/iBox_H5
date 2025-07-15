package com.joon.ibox_back_end.market.dto;

import lombok.Data;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-07-1510:45
 **/
@Data
public class FavoriteDto {
    private Integer userId;
    private Integer nftId;
    private Integer actionType; // 1:收藏, 0:取消
}
