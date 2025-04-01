package com.joon.ibox_back_end.wallet.controller;

import com.joon.ibox_back_end.common.PageResult;
import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.commonEntity.po.Transactions;
import com.joon.ibox_back_end.commonEntity.po.Wallet;
import com.joon.ibox_back_end.wallet.entity.RechargeRequest;
import com.joon.ibox_back_end.wallet.entity.WithdrawRequest;
import com.joon.ibox_back_end.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: backend
 * @description: 钱包控制类
 * @author: Joonnn
 * @create: 2025-04-0200:11
 **/

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping("/info")
    public R<Wallet> getWalletInfo(@RequestParam String walletAddress) {
        Wallet wallet = walletService.getWalletByAddress(walletAddress);
        return R.success(wallet);
    }

    @PostMapping("/recharge")
    public R<Wallet> recharge(@RequestBody RechargeRequest request) {
        Wallet wallet = walletService.recharge(request.getWalletAddress(), request.getAmount());
        return R.success(wallet);
    }

    @PostMapping("/withdraw")
    public R<Wallet> withdraw(@RequestBody WithdrawRequest request) {
        Wallet wallet = walletService.withdraw(request.getWalletAddress(), request.getAmount());
        return R.success(wallet);
    }

    @GetMapping("/transactions")
    public R getTransactions(@RequestParam String walletAddress) {
        try {
            List<Transactions> result = walletService.getTransactions(walletAddress);
            return R.success(result);
        } catch (Exception e) {
            e.printStackTrace(); // 添加这行查看完整异常
            return R.error("获取交易记录失败");
        }
    }


}
