package com.joon.ibox_back_end.wallet.service;

import com.joon.ibox_back_end.commonEntity.po.Transactions;
import com.joon.ibox_back_end.commonEntity.po.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {
    Wallet getWalletByAddress(String walletAddress);

    /**
     * 充值
     * @param walletAddress
     * @param amount
     * @return
     */
    Wallet recharge(String walletAddress, BigDecimal amount);

    /**
     * 提现
     * @param walletAddress
     * @param amount
     * @return
     */
    Wallet withdraw(String walletAddress, BigDecimal amount);

    List<Transactions> getTransactions(String walletAddress);


    boolean deductBalance(String userAddress, BigDecimal multiply);

    boolean refundBalance(String userAddress, BigDecimal multiply);
}
