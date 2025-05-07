package com.joon.ibox_back_end.login.mapper;

import com.joon.ibox_back_end.commonEntity.po.UserPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserLoginMapper {
    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    @Select("select * from users where phone_number = #{phone} ")
    UserPo findByPhone(String phone);

    /**
     * 新增新用户
     * @param user
     */
    void saveNewUser(UserPo user);
}
