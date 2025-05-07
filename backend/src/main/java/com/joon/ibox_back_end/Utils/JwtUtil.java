package com.joon.ibox_back_end.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import static sun.security.x509.CertificateX509Key.KEY;

/**
 * token生成器
 */
public class JwtUtil {
    // 密钥（建议使用更安全的存储方式，如环境变量或配置中心）
    private static final String SECRET_KEY = "9dc61dbc1ee3e1344658cd209d76f77d5db0232632e7c990c0b549c5b8b5ec80";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * 生成登录 Token
     */
    public static String createTokenForLogin(String phone) {
        long expTime = 120 * 60 * 1000; // 2H过期
        Date expiration = new Date(System.currentTimeMillis() + expTime);

        return Jwts.builder()
                .setSubject(phone)
                .claim("phone", phone) // 存储 phone 到 payload
                .setExpiration(expiration)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 Token，返回 phone（或其他字段）
     */
    public static String parseToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();

            return claims.get("phone", String.class); // 提取 phone
        } catch (Exception e) {
            throw new RuntimeException("Token 解析失败", e);
        }
    }
}

