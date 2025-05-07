package com.joon.ibox_back_end.login.service;

import com.joon.ibox_back_end.commonEntity.po.UserPo;

public interface UserLoginService {
    /**
     * 通过手机号查询用户信息
     * @param phone
     * @return
     */
    UserPo findByPhone(String phone);

    /**
     * 新增新用户
     */
    void saveNewUser(String phone);
}
