package com.joon.ibox_back_end.market.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: backend
 * @description: 寄售公告细节
 * @author: Joonnn
 * @create: 2025-03-2815:20
 **/

@Data
public class AnnounceDetailDto {
    private Integer id;
    private String title;
    private String content;
    private String publisher;
    private Boolean isPinned;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private String imageUrl;
    private String nftImageUrl;
}
