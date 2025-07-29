package com.joon.ibox_back_end.wallet.service.serviceImpl;

import com.joon.ibox_back_end.commonEntity.po.Transactions;
import com.joon.ibox_back_end.commonEntity.po.Wallet;
import com.joon.ibox_back_end.wallet.exception.BusinessException;
import com.joon.ibox_back_end.wallet.mapper.TransactionMapper;
import com.joon.ibox_back_end.wallet.mapper.WalletMapper;
import com.joon.ibox_back_end.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: backend
 * @description: 钱包实现类
 * @author: Joonnn
 * @create: 2025-04-0200:05
 **/
@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private TransactionMapper transactionMapper;
    public Wallet getWalletByAddress(String walletAddress) {
        Wallet wallet = walletMapper.findByWalletAddress(walletAddress);
        if (wallet == null) {
            throw new BusinessException("钱包不存在");

        }
        return wallet;
    }

    /**
     * 充值
     * @param walletAddress
     * @param amount
     * @return
     */
    @Transactional
    public Wallet recharge(String walletAddress, BigDecimal amount) {
        Wallet wallet = walletMapper.findByWalletAddress(walletAddress);
        if (wallet == null) {
            throw new BusinessException("钱包不存在");
        }

        walletMapper.updateBalance(wallet.getWalletId(), amount);
        return walletMapper.findByWalletAddress(walletAddress);
    }

    /**
     * 提现
     * @param walletAddress
     * @param amount
     * @return
     */
    @Transactional
    public Wallet withdraw(String walletAddress, BigDecimal amount) {
        Wallet wallet = walletMapper.findByWalletAddress(walletAddress);
        if (wallet == null) {
            throw new BusinessException("钱包不存在");
        }

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("余额不足");
        }

        walletMapper.updateBalance(wallet.getWalletId(), amount.negate());
        return walletMapper.findByWalletAddress(walletAddress);
    }

    @Transactional
    public List<Transactions> getTransactions(String walletAddress) {
        try {
            return transactionMapper.findByWalletAddress(walletAddress);
        } catch (Exception e) {
            throw new BusinessException("查询交易记录失败: " + e.getMessage());
        }
    }

    @Override
    public boolean deductBalance(String userAddress, BigDecimal amount) {
        try {
            int updated = walletMapper.deductBalance(userAddress, amount);
            return updated > 0;
        } catch (Exception e) {
            e.printStackTrace(); // 强制打印异常
            return false;
        }
    }

    @Override
    public boolean refundBalance(String userAddress, BigDecimal multiply) {
        return walletMapper.addBalance(userAddress, multiply) > 0;
    }


}