package com.joon.ibox_back_end.settings.service;

import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.commonEntity.po.UserPo;
import com.joon.ibox_back_end.settings.entity.ChangePhoneDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    UserPo findById(Integer userId);

    String updateUserAvatar(Integer userId, MultipartFile file) throws IOException;

    Integer findUserIdByPhone(String phone);

    void updateNickname(String phone, String nickname);

    R sendChangePhoneCode(String currentPhone, String newPhone);

    R changePhone(String currentPhone, ChangePhoneDTO changePhoneDTO);
}
