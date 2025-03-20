package com.joon.ibox_back_end.market.entity.po;

import lombok.Data;

/**
 * @program: backend
 * @description: 藏品标签
 * @author: Joonnn
 * @create: 2025-03-2001:26
 **/
@Data
public class NtfTag {
    /**
     * 主键，唯一标识每个标签。
     */
    private Integer id;

    /**
     * 标签的名称。
     */
    private String name;
}
