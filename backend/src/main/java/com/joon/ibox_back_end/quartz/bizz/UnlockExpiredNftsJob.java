package com.joon.ibox_back_end.quartz.bizz;

import com.joon.ibox_back_end.market.service.NftLockService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: backend
 * @description: 解锁过期的锁定订单
 * @author: Joonnn
 * @create: 2025-04-1011:42
 **/
@Component
public class UnlockExpiredNftsJob implements Job {
    @Autowired
    private NftLockService nftLockService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            nftLockService.unlockExpiredNfts();
        } catch (Exception e) {
            throw new JobExecutionException("解锁过期NFT任务执行失败", e);
        }
    }
}
