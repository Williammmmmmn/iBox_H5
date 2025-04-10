package com.joon.ibox_back_end.market.service.serviceImpl;

import com.joon.ibox_back_end.common.LockResponse;
import com.joon.ibox_back_end.commonEntity.po.Instances;
import com.joon.ibox_back_end.market.entity.LockRequest;
import com.joon.ibox_back_end.market.mapper.LockNftInstanceMapper;
import com.joon.ibox_back_end.market.service.NftLockService;
import com.joon.ibox_back_end.wallet.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static com.joon.ibox_back_end.common.Constants.LOCK_DURATION_MINUTES;

/**
 * @program: backend
 * @description: 购买时候锁定
 * @author: Joonnn
 * @create: 2025-04-1011:18
 **/
@Service
public class NftLockServiceImpl implements NftLockService {
    @Autowired
    private LockNftInstanceMapper lockNftInstanceMapper;

    @Transactional
    @Override
    public LockResponse lockNftInstance(LockRequest request) {
        Instances nftInstance = lockNftInstanceMapper.findByInstanceId(request.getInstanceId());
        if (nftInstance == null) {
            throw new BusinessException("NFT实例不存在");
        }
        // 获取当前时间（使用明确时区）
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        LocalDateTime expiry = now.plusMinutes(LOCK_DURATION_MINUTES);
        // 检查是否已被锁定
        if (nftInstance.getLockedBy() != null &&
                nftInstance.getLockExpiry() != null &&
                nftInstance.getLockExpiry().isAfter(now)){
            if (nftInstance.getLockedBy().equals(request.getWalletAddress())) {
                return new LockResponse(true, "您已成功锁定该NFT", nftInstance.getLockExpiry());
            } else {
                throw new BusinessException("该NFT已被其他用户锁定");
            }
        }

        // 设置锁定信息
        nftInstance.setLockedBy(request.getWalletAddress());
        nftInstance.setLockedAt(now);
        nftInstance.setLockExpiry(expiry);

        int updated = lockNftInstanceMapper.lockNftInstance(nftInstance);
        if (updated == 0) {
            throw new BusinessException("锁定NFT失败，可能已被其他用户修改");
        }

        return new LockResponse(true, "锁定成功", expiry);
    }
    public void unlockExpiredNfts() {
        lockNftInstanceMapper.unlockExpiredNfts();
    }
    @Override
    public void unlockNftInstance(Integer instanceId) {

    }
}
