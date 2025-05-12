package com.joon.ibox_back_end.homeSearch.entity;

import lombok.Data;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-05-1223:30
 **/
@Data
public class SearchResultDTO {
    private Integer id;
    private String name;
    private String imageUrl;
    private String minPrice;
}
