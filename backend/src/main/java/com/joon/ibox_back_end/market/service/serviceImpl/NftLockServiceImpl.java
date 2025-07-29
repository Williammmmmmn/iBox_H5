package com.joon.ibox_back_end.market.service.serviceImpl;

import com.joon.ibox_back_end.common.LockResponse;
import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.commonEntity.po.Instances;
import com.joon.ibox_back_end.market.dto.LockRequestDto;
import com.joon.ibox_back_end.market.mapper.LockNftInstanceMapper;
import com.joon.ibox_back_end.market.service.NftLockService;
import com.joon.ibox_back_end.wallet.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static com.joon.ibox_back_end.common.Constants.*;

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
    public LockResponse lockNftInstance(LockRequestDto request) {
        // 首先检查当前实例的锁定状态
        Instances currentInstance = lockNftInstanceMapper.findByInstanceId(request.getInstanceId());
        if (currentInstance == null) {
            throw new BusinessException(ERROR_NFT_NOT_EXIST);
        }

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));

        // 如果是用户自己已锁定的实例，直接返回成功
        if (currentInstance.getLockedBy() != null &&
                currentInstance.getLockedBy().equals(request.getWalletAddress()) &&
                currentInstance.getLockExpiry() != null &&
                currentInstance.getLockExpiry().isAfter(now)) {
            return new LockResponse(true, "继续购买流程", currentInstance.getLockExpiry());
        }

        // 检查用户是否锁定了其他实例
        List<Instances> userLockedInstances = lockNftInstanceMapper.findLockedByUser(request.getWalletAddress())
                .stream()
                .filter(instance -> !instance.getInstanceId().equals(request.getInstanceId()))
                .collect(Collectors.toList());

        if (!userLockedInstances.isEmpty()) {
            for (Instances instance : userLockedInstances) {
                if (instance.getLockExpiry() != null && instance.getLockExpiry().isAfter(now)) {
                    throw new BusinessException(ERROR_PENDING_ORDER);
                }
            }
        }

        // 如果当前实例被其他人锁定
        if (currentInstance.getLockedBy() != null &&
                currentInstance.getLockExpiry() != null &&
                currentInstance.getLockExpiry().isAfter(now)) {
            throw new BusinessException(ERROR_ALREADY_LOCKED);
        }

        // 设置锁定信息
        currentInstance.setLockedBy(request.getWalletAddress());
        currentInstance.setLockedAt(now);
        currentInstance.setLockExpiry(now.plusMinutes(LOCK_DURATION_MINUTES));

        int updated = lockNftInstanceMapper.lockNftInstance(currentInstance);
        if (updated == 0) {
            throw new BusinessException(ERROR_LOCK_FAILED);
        }

        return new LockResponse(true, "锁定成功", currentInstance.getLockExpiry());
    }
    public void unlockExpiredNfts() {
        lockNftInstanceMapper.unlockExpiredNfts();
    }
    @Override
    public R unlockNftInstance(Integer instanceId) {
        // 1. 查询NFT实例信息
        Instances instance = lockNftInstanceMapper.findByInstanceId(instanceId);
        if (instance == null) {
            throw new BusinessException(ERROR_NFT_NOT_EXIST);
        }

        // 2. 检查锁定状态
        if (instance.getLockedBy() == null) {
            throw new BusinessException("该藏品未被锁定");
        }

        // 3. 解锁操作
        int updated = lockNftInstanceMapper.unlockNftInstance(instanceId);
        if (updated == 0) {
            throw new BusinessException("解锁失败，请稍后再试");
        }

        return R.success("解锁成功");
    }
}
