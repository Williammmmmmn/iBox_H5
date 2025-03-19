package com.joon.ibox_back_end.market.eneity;

import lombok.Data;

/**
 * @program: backend
 * @description: 藏品所属等级(普通、高级、传说等)
 * @author: Joonnn
 * @create: 2025-03-2001:33
 **/
@Data
public class Levels {
    /**
     * 主键，唯一标识每个等级。
     */
    private Integer id;

    /**
     * 等级名称（如传说、高级、普通）。
     */
    private String name;
}
