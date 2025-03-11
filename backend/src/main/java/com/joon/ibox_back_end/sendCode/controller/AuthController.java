package com.joon.ibox_back_end.sendCode.controller;

import com.joon.ibox_back_end.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @program: iBox_back_end
 * @description: 生成验证码存入redis
 * @author: Joonnn
 * @create: 2025-03-1115:21
 **/

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/sendCode")
    public R sendVerificationCode(@RequestParam String phone) {
        // 生成 6 位随机验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

        // 将验证码存入 Redis，设置过期时间为 5 分钟
        redisTemplate.opsForValue().set("code:" + phone, code, 1, TimeUnit.MINUTES);

        // 模拟发送验证码（实际项目中可以调用短信服务）
        System.out.println("验证码已发送至 " + phone + "，验证码为：" + code);

        return R.success("验证码已发送");
    }
}
