package com.joon.ibox_back_end.market.mapper;

import com.joon.ibox_back_end.commonEntity.po.Instances;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface PurchaseMapper {
    String getSellerAddress(@Param("instanceId") Integer instanceId);

    String getCurrentOwnerWithLock(@Param("instanceId") Integer instanceId);

    // 新增方法：获取NFT实例信息（带行锁）
    @Select("SELECT instance_id, nft_id, owner_address, version " +
            "FROM nft_instances WHERE instance_id = #{instanceId} FOR UPDATE")
    Instances getNftInstanceWithLock(@Param("instanceId") Integer instanceId);
    int getNftIdByInstanceId(@Param("instanceId") Integer instanceId);

    void updateConsignmentStatus(
            @Param("instanceId") Integer instanceId,
            @Param("buyerWalletAddress") String buyerWalletAddress);

    @Update("UPDATE nft_instances " +
            "SET owner_address = #{newOwnerWalletAddress}, " +
            "    version = version + 1, " +
            "    is_consigning = 0 " +
            "WHERE instance_id = #{instanceId} " +
            "AND version = #{expectedVersion}")
    int updateNftInstanceOwner(
            @Param("instanceId") Integer instanceId,
            @Param("newOwnerWalletAddress") String newOwnerWalletAddress,
            @Param("expectedVersion") Integer expectedVersion);

    void insertTransaction(
            @Param("nftId") Integer nftId,
            @Param("fromWalletAddress") String fromWalletAddress,
            @Param("toWalletAddress") String toWalletAddress,
            @Param("transactionHash") String transactionHash,
            @Param("price") BigDecimal price,
            @Param("currency") String currency,
            @Param("instanceId") Integer instanceId);

    int updateBuyerWallet(
            @Param("buyerWalletAddress") String buyerWalletAddress,
            @Param("price") BigDecimal price);

    void updateSellerWallet(
            @Param("sellerWalletAddress") String sellerWalletAddress,
            @Param("sellerAmount") BigDecimal sellerAmount);

    BigDecimal getWalletBalance(@Param("walletAddress") String walletAddress);

    @Select("SELECT COUNT(*) FROM transactions " +
            "WHERE instance_id = #{instanceId} " +
            "AND from_address = #{sellerWalletAddress} " +
            "AND to_address = #{buyerWalletAddress} " +
            "AND type = 'purchase'")
    int checkTransactionExists(
            @Param("instanceId") Integer instanceId,
            @Param("sellerWalletAddress") String sellerWalletAddress,
            @Param("buyerWalletAddress") String buyerWalletAddress
            );
}