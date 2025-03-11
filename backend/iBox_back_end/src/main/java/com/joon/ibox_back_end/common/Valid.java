package com.joon.ibox_back_end.common;

/**
 * @program: iBox_back_end
 * @description: 校验
 * @author: Joonnn
 * @create: 2025-03-1111:27
 **/

public class Valid {
    // 验证手机号格式
    public boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^1[3-9]\\d{9}$");
    }

    // 验证验证码格式
    public boolean isValidVerificationCode(String verificationCode) {
        return verificationCode != null && verificationCode.length() == 6;
    }
}
