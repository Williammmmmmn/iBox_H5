package com.joon.ibox_back_end.wallet.mapper;

import com.joon.ibox_back_end.commonEntity.po.Wallet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface WalletMapper {

    @Select("SELECT * FROM wallets WHERE wallet_address = #{walletAddress}")
    Wallet findByWalletAddress(String walletAddress);

    @Update("UPDATE wallets SET balance = balance + #{amount}, updated_at = NOW() WHERE wallet_id = #{walletId}")
    int updateBalance(@Param("walletId") Integer walletId, @Param("amount") BigDecimal amount);
}
