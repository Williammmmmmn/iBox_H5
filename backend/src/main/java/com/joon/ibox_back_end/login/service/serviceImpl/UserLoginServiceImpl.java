package com.joon.ibox_back_end.login.service.serviceImpl;

import com.joon.ibox_back_end.commonEntity.po.PersonalPo;
import com.joon.ibox_back_end.utils.RandomUsernameGenerator;
import com.joon.ibox_back_end.login.mapper.UserLoginMapper;
import com.joon.ibox_back_end.login.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: iBox_back_end
 * @description: 用户登录实现类
 * @author: Joonnn
 * @create: 2025-03-1115:29
 **/
@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    private UserLoginMapper userLoginMapper;

    /**
     * 通过手机号查询用户
     * @param phone
     * @return
     */
    @Override
    public PersonalPo findByPhone(String phone) {
        PersonalPo user = userLoginMapper.findByPhone(phone);
        if(user != null){
            return user;
        }
        return null;
    }

    /**
     * 新增新用户
     * @param phone
     */
    @Override
    public void saveNewUser(String phone) {
        PersonalPo user = new PersonalPo();
        user.setPhoneNumber(phone);
//        user.setCreatedAt(LocalDateTime.now());
        user.setUsername(RandomUsernameGenerator.generateRandomUsername());
        user.setRole("user");
        user.setStatus("active");
        // 设置默认值或处理空字段
        if (user.getPassword() == null) {
            user.setPassword("123456"); // 设置默认密码或留空
        }
        userLoginMapper.saveNewUser(user);
    }
}
