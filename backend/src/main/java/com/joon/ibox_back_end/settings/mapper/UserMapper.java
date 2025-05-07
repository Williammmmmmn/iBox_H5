package com.joon.ibox_back_end.settings.mapper;

import com.joon.ibox_back_end.commonEntity.po.UserPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    UserPo findById(Integer userId);

    @Update("UPDATE users SET avatar = #{avatar} WHERE user_id = #{userId}")
    void updateAvatar(@Param("userId") Integer userId, @Param("avatar") String avatar);

    @Select("SELECT user_id FROM users WHERE phone_number = #{phone}")
    Integer findUserIdByPhone(String phone);

    @Update("UPDATE users SET username = #{nickname} WHERE phone_number = #{phone}")
    void updateNickname(@Param("phone") String phone, @Param("nickname") String nickname);

    @Update("UPDATE users SET phone_number = #{newPhone} WHERE phone_number = #{currentPhone}")
    void updatePhone(@Param("currentPhone")String currentPhone, @Param("newPhone")String newPhone);

    @Select("SELECT COUNT(*) FROM users WHERE phone_number = #{phone}")
    boolean existsByPhone(String newPhone);
}
