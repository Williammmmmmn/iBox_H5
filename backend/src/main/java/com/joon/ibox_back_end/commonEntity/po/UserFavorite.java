package com.joon.ibox_back_end.commonEntity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @program: backend
 * @description: 用户收藏表
 * @author: Joonnn
 * @create: 2025-07-0118:30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFavorite {
    private Long id;
    private Integer userId;
    private Integer nftId;
    private LocalDateTime createdAt;
}
