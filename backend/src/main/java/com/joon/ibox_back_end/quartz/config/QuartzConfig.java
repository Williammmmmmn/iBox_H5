package com.joon.ibox_back_end.quartz.config;

import com.joon.ibox_back_end.quartz.bizz.UnlockExpiredNftsJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: backend
 * @description: quartz定时任务配置类
 * @author: Joonnn
 * @create: 2025-04-1011:40
 **/
@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail unlockExpiredNftsJobDetail() {
        return JobBuilder.newJob(UnlockExpiredNftsJob.class)
                .withIdentity("unlockExpiredNftsJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger unlockExpiredNftsTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(5) // 每5分钟执行一次
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(unlockExpiredNftsJobDetail())
                .withIdentity("unlockExpiredNftsTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
