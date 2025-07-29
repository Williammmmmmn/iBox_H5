package com.joon.ibox_back_end.wallet.mapper;

import com.joon.ibox_back_end.commonEntity.po.Wallet;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

@Mapper
public interface WalletMapper {

    @Select("SELECT * FROM wallets WHERE wallet_address = #{walletAddress}")
    Wallet findByWalletAddress(String walletAddress);

    @Update("UPDATE wallets SET balance = balance + #{amount}, updated_at = NOW() WHERE wallet_id = #{walletId}")
    int updateBalance(@Param("walletId") Integer walletId, @Param("amount") BigDecimal amount);

    int insertWallet(Wallet wallet);

    BigDecimal selectBalanceByAddress(@Param("walletAddress") String walletAddress);
    @Options(useGeneratedKeys = false, keyProperty = "none")
    int deductBalance(@Param("walletAddress")String userAddress, @Param("amount")BigDecimal amount);

    int addBalance(String userAddress, BigDecimal multiply);
}
