package com.joon.ibox_back_end.login.entity;

import com.joon.ibox_back_end.commonEntity.po.PersonalPo;
import lombok.Data;

/**
 * @program: iBox_back_end
 * @description: 用户信息拓展
 * @author: Joonnn
 * @create: 2025-03-1111:32
 **/
@Data
public class UserDto extends PersonalPo {
    private String token;
}
