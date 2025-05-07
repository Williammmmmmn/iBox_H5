package com.joon.ibox_back_end.settings.service.Impl;

import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.commonEntity.po.UserPo;
import com.joon.ibox_back_end.settings.entity.ChangePhoneDTO;
import com.joon.ibox_back_end.settings.mapper.UserMapper;
import com.joon.ibox_back_end.settings.service.UserService;
import com.joon.ibox_back_end.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @program: backend
 * @description: 用户个人信息实现类
 * @author: Joonnn
 * @create: 2025-04-2917:46
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.access-path}")
    private String accessPath;
    @Override
    public UserPo findById(Integer userId) {
        return userMapper.findById(userId);
    }

    @Override
    public String updateUserAvatar(Integer userId, MultipartFile file) throws IOException {
        // 保存文件到服务器
        String filename = FileUploadUtil.saveFile(uploadDir, file);

        // 构建访问路径
        String avatarUrl = accessPath + "/" + filename;

        // 更新数据库
        userMapper.updateAvatar(userId, avatarUrl);

        return avatarUrl;
    }

    @Override
    public Integer findUserIdByPhone(String phone) {
        return userMapper.findUserIdByPhone(phone);
    }

    @Override
    public void updateNickname(String phone, String nickname) {
        // 检查昵称是否为空
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("昵称不能为空");
        }
        // 更新数据库
        userMapper.updateNickname(phone, nickname);
    }

    @Override
    public R sendChangePhoneCode(String currentPhone, String newPhone) {
        // 验证新手机号格式
        if (!newPhone.matches("^1[3-9]\\d{9}$")) {
            return R.error("手机号格式不正确");
        }

        // 检查是否与当前手机号相同
        if (currentPhone.equals(newPhone)) {
            return R.error("新手机号不能与当前手机号相同");
        }

        // 检查手机号是否已被使用
        if (userMapper.existsByPhone(newPhone)) {
            return R.error("该手机号已被使用");
        }

        // 生成6位随机验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

        // 存储验证码到Redis，key为 changePhone:{currentPhone}:{newPhone}
        String redisKey = String.format("changePhone:%s:%s", currentPhone, newPhone);
        redisTemplate.opsForValue().set(redisKey, code, 5, TimeUnit.MINUTES);

        // 模拟发送验证码
        System.out.println("手机号更换验证码: " + code);

        return R.success("验证码已发送");
    }

    @Override
    public R changePhone(String currentPhone, ChangePhoneDTO dto) {
        // 验证新手机号格式
        if (!dto.getNewPhone().matches("^1[3-9]\\d{9}$")) {
            return R.error("手机号格式不正确");
        }

        // 检查是否与当前手机号相同
        if (currentPhone.equals(dto.getNewPhone())) {
            return R.error("新手机号不能与当前手机号相同");
        }

        // 检查手机号是否已被使用
        if (userMapper.existsByPhone(dto.getNewPhone())) {
            return R.error("该手机号已被使用");
        }

        // 验证验证码
        String redisKey = String.format("changePhone:%s:%s", currentPhone, dto.getNewPhone());
        String correctCode = redisTemplate.opsForValue().get(redisKey);

        if (correctCode == null) {
            return R.error("验证码已过期，请重新获取");
        }

        if (!correctCode.equals(dto.getCode())) {
            return R.error("验证码不正确");
        }

        // 更新手机号
        userMapper.updatePhone(currentPhone, dto.getNewPhone());

        // 删除验证码缓存
        redisTemplate.delete(redisKey);

        return R.success("手机号更换成功");
    }
}
