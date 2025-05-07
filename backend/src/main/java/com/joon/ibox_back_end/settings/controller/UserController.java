package com.joon.ibox_back_end.settings.controller;

import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.commonEntity.po.UserPo;
import com.joon.ibox_back_end.settings.entity.ChangePhoneDTO;
import com.joon.ibox_back_end.settings.service.UserService;
import com.joon.ibox_back_end.utils.JwtUtil;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-04-2917:47
 **/

@RestController
@RequestMapping("/setting")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public R getUserInfo(@RequestHeader("Authorization") String token)  {
        String phone = JwtUtil.parseToken(token.replace("Bearer ", ""));
        Integer userId = userService.findUserIdByPhone(phone);
        UserPo user = userService.findById(userId);
        return R.success(user);
    }
    /**
     * 更新用户头像
     * @param token
     * @param file
     * @return
     */
    @PostMapping("/uploadAvatar")
    public R uploadAvatar(
            @RequestHeader("Authorization") String token,
            @RequestParam("avatar") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return R.error("请选择要上传的文件");
        }
        String phone = JwtUtil.parseToken(token.replace("Bearer ", ""));
        Integer userId = userService.findUserIdByPhone(phone);
        String avatarUrl = userService.updateUserAvatar(userId, file);
        return R.success(avatarUrl);
    }


    /**
     * 更新用户昵称
     * @param token
     * @param request
     * @return
     */
    @PostMapping("/updateNickname")
    public R updateNickname(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> request) {

        String nickname = request.get("nickname");
        if (StringUtils.isEmpty(nickname)) {
            return R.error("昵称不能为空");
        }
        String phone = JwtUtil.parseToken(token.replace("Bearer ", ""));
        userService.updateNickname(phone, nickname);
        return R.success("昵称修改成功");
    }


    @PostMapping("/sendChangePhoneCode")
    public R sendChangePhoneCode(@RequestHeader("Authorization") String token, // 从token解析的当前用户手机号
                                 @RequestBody Map<String, String> request) {
        String newPhone = request.get("newPhone");
        if (StringUtils.isEmpty(newPhone)) {
            return R.error("新输入手机号不能为空");
        }
        String currentPhone = JwtUtil.parseToken(token.replace("Bearer ", ""));
        return userService.sendChangePhoneCode(currentPhone, newPhone);
    }

    @PostMapping("/changePhone")
    public R changePhone(@RequestHeader("Authorization") String token, // 从token解析的当前用户手机号
                         @RequestBody ChangePhoneDTO changePhoneDTO) {
        String currentPhone = JwtUtil.parseToken(token.replace("Bearer ", ""));

        return userService.changePhone(currentPhone, changePhoneDTO);
    }

}
