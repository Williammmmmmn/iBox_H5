package com.joon.ibox_back_end.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @program: backend
 * @description: 返回类
 * @author: Joonnn
 * @create: 2025-04-1010:55
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LockResponse  {
    private Boolean success;
    private String message;
    private LocalDateTime lockExpiry;


}
