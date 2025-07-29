package com.joon.ibox_back_end.announcements.entity;

import lombok.Data;

/**
 * @program: backend
 * @description: 公告列表查询参数dto
 * @author: Joonnn
 * @create: 2025-05-1918:21
 **/
@Data
public class AnnouncementQueryDto {
    private String keyword;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
