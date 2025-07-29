package com.joon.ibox_back_end.market.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-03-2815:08
 **/
@Data
public class NftAnnounceDto {
    private Long id;
    private String title;
    private LocalDateTime startTime; // 只返回必要字段
}
