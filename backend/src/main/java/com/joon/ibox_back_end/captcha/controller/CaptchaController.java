package com.joon.ibox_back_end.captcha.controller;

import com.joon.ibox_back_end.common.R;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @program: backend
 * @description: 图片验证码controller
 * @author: Joonnn
 * @create: 2025-03-1215:40
 **/
@RestController
public class CaptchaController {
    @Autowired
    private StringRedisTemplate redisTemplate; // 注入 Redis 操作工具
    /**
     * 生成图片验证码
     */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response) throws IOException {
        // 设置响应头为图片类型
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-store, no-cache");

        // 生成验证码（130x48 大小，4 位字符）
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT); // 设置字符类型

        // 生成唯一的验证码标识（UUID）
        String captchaKey = UUID.randomUUID().toString();
        String captchaText = specCaptcha.text().toLowerCase();

        // 将验证码文本存入 Redis，设置过期时间为 5 分钟
        redisTemplate.opsForValue().set(captchaKey, captchaText, 1, TimeUnit.MINUTES);

        // 将验证码标识返回给前端（通过响应头）
        response.setHeader("Captcha-Key", captchaKey);

        // 将验证码图片写入响应流
        specCaptcha.out(response.getOutputStream());
    }
    /**
     * 验证图片验证码
     */
    @PostMapping("/validate-captcha")
    public R<Object> validateCaptcha(@RequestBody Map<String, String> requestBody) {
        String userCaptcha = requestBody.get("captcha"); // 用户输入的验证码
        String captchaKey = requestBody.get("captchaKey"); // 验证码标识

        // 从 Redis 中获取验证码文本
        String captchaText = redisTemplate.opsForValue().get(captchaKey);

        // 验证结果
        boolean isCaptchaValid = captchaText != null && captchaText.equalsIgnoreCase(userCaptcha);

        // 验证成功后删除 Redis 中的验证码
        if (isCaptchaValid) {
            redisTemplate.delete(captchaKey);
        }

        // 根据验证结果返回不同的响应
        if (isCaptchaValid) {
            return R.success("验证成功"); // 或者 R.success() 如果不需要返回任何数据
        } else {
            return R.error("图片验证失败"); // 这里可以根据实际情况返回错误信息，并设置相应的状态码
        }
    }

}
