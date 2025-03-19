package com.joon.ibox_back_end.market.eneity;

import lombok.Data;

/**
 * @program: backend
 * @description: 藏品发行份数
 * @author: Joonnn
 * @create: 2025-03-2001:30
 **/
@Data
public class Score {
    /**
     * 主键，唯一标识每个藏品发行份数。
     */
    private Integer id;

    /**
     * 发行份数。
     */
    private Integer score;
}
