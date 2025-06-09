package com.joon.ibox_back_end.commonEntity.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-05-1918:30
 **/
@Data
public class Announcements {
    private Long id;
    private Long nftId;
    private String imageUrl;
    private String title;
    private String content;
    private String publisher;
    private Boolean isPinned;
    private String image;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
