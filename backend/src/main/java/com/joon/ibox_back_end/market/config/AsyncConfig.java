package com.joon.ibox_back_end.market.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @program: backend
 * @description: 异步配置
 * @author: Joonnn
 * @create: 2025-04-1623:23
 **/

@Configuration
@EnableAsync  // 启用异步支持
public class AsyncConfig {

    @Bean(name = "purchaseTaskExecutor")  // 定义线程池
    public Executor purchaseTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);      // 核心线程数
        executor.setMaxPoolSize(10);      // 最大线程数
        executor.setQueueCapacity(100);   // 队列容量
        executor.setThreadNamePrefix("Purchase-Async-");  // 线程名前缀
        executor.initialize();
        return executor;
    }
}
