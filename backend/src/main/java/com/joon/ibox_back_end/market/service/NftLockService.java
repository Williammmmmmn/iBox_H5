package com.joon.ibox_back_end.market.service;

import com.joon.ibox_back_end.common.LockResponse;
import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.market.entity.LockRequestDto;

public interface NftLockService {
    /**
     * 锁定NFT实例
     *
     * @param request 锁定请求
     * @return 锁定响应
     */
    LockResponse lockNftInstance(LockRequestDto request);
    /**
     * 解锁NFT实例
     *
     * @param instanceId 实例ID
     */
    R unlockNftInstance(Integer instanceId);

    /**
     * 解锁过期的NFT实例
     *
     */
    void unlockExpiredNfts();
}
