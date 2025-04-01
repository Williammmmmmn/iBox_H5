package com.joon.ibox_back_end.utils;

import io.jsonwebtoken.*;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器，用于验证客户端发送的JWT Token，解析token获取phone
 */

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final String secretKey;

    public JwtAuthenticationFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 从请求头中获取JWT Token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 移除"Bearer "前缀

            try {
                // 解析JWT Token
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                        .getBody();
                // 获取phone
                String phone = claims.get("phone", String.class);
                // 将openid存储在请求属性中，以便后续使用
                request.setAttribute("phone", phone);
            } catch (ExpiredJwtException e) {
                // Token过期
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"statusCode\":\"401\",\"error\":\"Token expired\",\"message\":\"tokenExpired\"}");
                return;
            } catch (MalformedJwtException | UnsupportedJwtException e) {
                // Token无效
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"statusCode\":\"401\",\"error\":\"Invalid token\",\"message\":\"tokenInvalid\"}");
                return;
            } catch (Exception e) {
                // 其他异常
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                response.getWriter().write("{\"statusCode\":\"500\",\"error\":\"Internal server error\",\"message\":\"unknownError\"}");
                return;
            }
        }
        // 继续过滤器链
        filterChain.doFilter(request, response);
    }
}
