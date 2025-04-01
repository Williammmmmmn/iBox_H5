package com.joon.ibox_back_end.wallet.mapper;


import com.joon.ibox_back_end.commonEntity.po.Transactions;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionMapper {
    /**
     * 查询交易记录
     * @param walletAddress

     * @return
     */
    List<Transactions> findByWalletAddress(String walletAddress);
}
