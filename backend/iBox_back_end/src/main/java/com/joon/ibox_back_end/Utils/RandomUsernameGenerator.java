package com.joon.ibox_back_end.Utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @program: iBox_back_end
 * @description: 随机生成用户名
 * @author: Joonnn
 * @create: 2025-03-1115:43
 **/

public class RandomUsernameGenerator {
    public static String generateRandomUsername() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder username = new StringBuilder();

        // 使用 ThreadLocalRandom 生成随机索引
        for (int i = 0; i < 6; i++) {
            int index = ThreadLocalRandom.current().nextInt(characters.length());
            username.append(characters.charAt(index));
        }

        return username.toString();
    }
}
