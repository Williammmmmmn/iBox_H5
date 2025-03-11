package com.joon.ibox_back_end.login.controller;

import com.joon.ibox_back_end.Utils.RandomUsernameGenerator;
import com.joon.ibox_back_end.common.CreateTokenForLogin;
import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.common.Valid;
import com.joon.ibox_back_end.login.entity.LoginRequest;
import com.joon.ibox_back_end.login.entity.UserDto;
import com.joon.ibox_back_end.login.entity.UserPo;
import com.joon.ibox_back_end.login.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @program: iBox_back_end
 * @description: 用户登录
 * @author: Joonnn
 * @create: 2025-03-1111:15
 **/
@RestController
@RequestMapping("/user")
public class UserLoginController {
    private final Valid valid = new Valid();
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserLoginService userLoginService;
    @PostMapping("/login")
    public R login(@RequestBody LoginRequest request) {
        String phone = request.getPhone();
        String verificationCode = request.getVerificationCode();
        String invitationCode = request.getInvitationCode();
        // 1. 验证手机号和验证码
        if (!valid.isValidPhone(phone)) {
            return R.error("手机号格式错误");
        }
        if (!valid.isValidVerificationCode(verificationCode)) {
            return R.error("验证码格式错误");
        }
        // 从 Redis 中获取验证码
        String storedCode = redisTemplate.opsForValue().get("code:" + phone);
        if (storedCode == null) {
            return R.error("验证码已过期");
        }

        if (!storedCode.equals(verificationCode)) {
            return R.error("验证码错误");
        }
        // 验证码正确，判断用户是否存在
        UserPo user = userLoginService.findByPhone(phone);
        if (user == null) {
            // 用户不存在，创建新用户
            userLoginService.saveNewUser(phone);
            // 返回用户信息
        }
        // 生成 Token
        String token = CreateTokenForLogin.generateToken(phone);
        // 创建返回的 Data 对象
        Map<String, Object> data = createResponseData(user, token);
        return R.success("登录成功", data);
    }

    private Map<String, Object> createResponseData(UserPo user, String token) {
        Map<String, Object> data = new HashMap<>();
        data.put("user", user); // 用户信息
        data.put("token", token); // Token
        return data;
    }

}
