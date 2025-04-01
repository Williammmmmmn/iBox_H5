package com.joon.ibox_back_end.common;

import com.joon.ibox_back_end.utils.JwtUtil;

/**
 * @program: iBox_back_end
 * @description: 创建登录时token
 * @author: Joonnn
 * @create: 2025-03-1117:25
 **/

public class CreateTokenForLogin {
    /**
     * 生成登录用的JWT Token
     *
     * @param phone 用户手机号
     * @return 生成的JWT Token
     */
    public static String generateToken(String phone) {
        return JwtUtil.createTokenForLogin(phone);
    }
}
