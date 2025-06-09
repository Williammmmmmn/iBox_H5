package com.joon.ibox_back_end.common;

import lombok.Data;

import java.util.List;

/**
 * @program: backend
 * @description: 分页工具类
 * @author: Joonnn
 * @create: 2025-04-0200:08
 **/

@Data
public class PageResult<T> {
    private List<T> list;
    private int pageNum;
    private int pageSize;
    private long total;
    private int pages;

    public PageResult(List<T> list, int pageNum, int pageSize, long total) {
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = (int) Math.ceil((double) total / pageSize);
    }

    public PageResult() {

    }
}