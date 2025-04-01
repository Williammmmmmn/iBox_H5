package com.joon.ibox_back_end.personalHub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: backend
 * @description: 用户个人中心dto
 * @author: Joonnn
 * @create: 2025-04-0100:42
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private String username;
    private String avatar;
    private String walletAddress;
}
