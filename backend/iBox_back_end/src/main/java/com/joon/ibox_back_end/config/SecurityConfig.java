package com.joon.ibox_back_end.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF保护
                .csrf().disable()
                // 允许所有请求，不需要认证
                .authorizeRequests().anyRequest().permitAll()
                // 禁用HTTP基本认证
                .and().httpBasic().disable()
                // 禁用表单登录
                .formLogin().disable();
    }
}
