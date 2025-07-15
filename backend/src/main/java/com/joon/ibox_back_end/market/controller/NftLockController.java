package com.joon.ibox_back_end.market.controller;

import com.joon.ibox_back_end.common.LockResponse;
import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.market.dto.LockRequestDto;
import com.joon.ibox_back_end.market.service.NftLockService;
import com.joon.ibox_back_end.wallet.exception.BusinessException;
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
    private NftLockService nftLockService;

    @PostMapping("/lock")
    public R lockNft(@RequestBody LockRequestDto request) {
        try {
            LockResponse lockResponse = nftLockService.lockNftInstance(request);
            return R.success(lockResponse);
        } catch (BusinessException e) {
            // 直接传递异常消息
            return R.error(e.getMessage());
        } catch (Exception e) {
            return R.error("系统错误，请稍后再试");
        }
    }

    @PostMapping("/unlock/{instanceId}")
    public R unlockNft(@PathVariable Integer instanceId) {
        try {
            nftLockService.unlockNftInstance(instanceId);
            return R.success("取消成功");
        } catch (BusinessException e) {
            return R.error(e.getMessage());
        }
    }
}