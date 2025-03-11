package com.joon.ibox_back_end.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * token生成器
 */
public class JwtUtil {
    //密钥
    private static final String key = "9dc61dbc1ee3e1344658cd209d76f77d5db0232632e7c990c0b549c5b8b5ec80";

    public static String createTokenForLogin(String phone) {
        //设置token的过期时间
        long expTime = 15 * 60 *1000;//过期时间20min
        Date expiration = new Date(System.currentTimeMillis() + expTime);
        // 使用Auth0的JWT库创建Token

        String token = Jwts.builder()
                .setSubject(phone)
                .claim("openid", phone) // 添加参数到payload
                .setExpiration(expiration) // 设置过期时间
                .signWith(SignatureAlgorithm.HS256, key) // 使用HS256算法和密钥签名
                .compact();
        return token;
    }
}

