package com.joon.ibox_back_end.market.controller;

import com.joon.ibox_back_end.common.LockResponse;
import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.market.entity.LockRequestDto;
import com.joon.ibox_back_end.market.service.NftLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: backend
 * @description: 购买时锁定
 * @author: Joonnn
 * @create: 2025-04-1011:19
 **/
@RestController
@RequestMapping("/buyLock")
public class NftLockController {
    @Autowired
    private NftLockService  nftLockService;

    @PostMapping("/lock")
    public R lockNft(@RequestBody LockRequestDto request) {
        try {
            LockResponse lockResponse = nftLockService.lockNftInstance(request);
            return R.success(lockResponse);
        } catch (Exception e) {
            return R.error("已被其他用户锁定");
        }
    }

    @PostMapping("/unlock/{instanceId}")
    public void unlockNft(@PathVariable Integer instanceId) {
        nftLockService.unlockNftInstance(instanceId);
    }
}
