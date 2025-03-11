package com.joon.ibox_back_end.login.entity;

import lombok.Data;

/**
 * @program: iBox_back_end
 * @description: 登录请求体
 * @author: Joonnn
 * @create: 2025-03-1112:22
 **/
@Data
public class LoginRequest {
    private String phone;
    private String verificationCode;
    private String invitationCode;


}
