package com.joon.ibox_back_end.announcements.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-05-1918:20
 **/
@Data
public class AnnouncementDto {
    private Long id;
    private Long nftId;
    private String title;
    private String content;
    private String publisher;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imageUrl;
}
