package com.joon.ibox_back_end.market.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface PurchaseMapper {
    // 方法参数中的钱包地址统一使用 walletAddress 命名
    String getSellerAddress(@Param("instanceId") Integer instanceId);

    int getNftIdByInstanceId(@Param("instanceId") Integer instanceId);

    void updateConsignmentStatus(
            @Param("instanceId") Integer instanceId,
            @Param("buyerWalletAddress") String buyerWalletAddress);

    void updateNftInstanceOwner(
            @Param("instanceId") Integer instanceId,
            @Param("newOwnerWalletAddress") String newOwnerWalletAddress);

    void insertTransaction(
            @Param("nftId") Integer nftId,
            @Param("fromWalletAddress") String fromWalletAddress,
            @Param("toWalletAddress") String toWalletAddress,
            @Param("transactionHash") String transactionHash,
            @Param("price") BigDecimal price,
            @Param("currency") String currency,
            @Param("instanceId") Integer instanceId);

    void updateBuyerWallet(
            @Param("buyerWalletAddress") String buyerWalletAddress,
            @Param("price") BigDecimal price);

    void updateSellerWallet(
            @Param("sellerWalletAddress") String sellerWalletAddress,
            @Param("sellerAmount") BigDecimal sellerAmount);
}