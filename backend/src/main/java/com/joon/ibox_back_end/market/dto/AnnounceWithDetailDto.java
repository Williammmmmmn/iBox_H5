package com.joon.ibox_back_end.market.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-04-0214:52
 **/

@Data
public class AnnounceWithDetailDto {
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