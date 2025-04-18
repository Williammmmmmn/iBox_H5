package com.joon.ibox_back_end.market.mapper;

import com.joon.ibox_back_end.commonEntity.po.Instances;
import com.joon.ibox_back_end.commonEntity.po.PurchaseRequests;
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
    @Select("SELECT instance_id, nft_id, owner_address, version,is_consigning " +
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
            "  AND is_consigning = 1 " +
            "  AND version = #{expectedVersion}")
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

  //以下是售出给求购者
    // 根据藏品编号和NFT ID获取NFT实例（带锁）
    Instances getNftInstanceByNumberAndNftIdWithLock(@Param("instanceNumber") Integer instanceNumber,
                                                     @Param("nftId") Integer nftId);

    // 获取匹配的对应求购记录。来看买家地址的
    PurchaseRequests getActivePurchaseRequestByNftIdAndPrice(@Param("nftId") Integer nftId,
                                                             @Param("price") BigDecimal price);
    // 检查是否已存在出售交易
    int checkSellTransactionExists(@Param("purchaseRequestId") Integer purchaseRequestId,
                                   @Param("instanceId") Integer instanceId );
//    为求购更新藏品实例拥有者
    int updateNftInstanceOwnerForPurchaseRequest(
            @Param("instanceId") Integer instanceId,
            @Param("newOwnerWalletAddress") String newOwnerWalletAddress,
            @Param("expectedVersion") Integer expectedVersion);

    // 更新求购记录状态
    int updatePurchaseRequestStatus(@Param("id") Integer id,
                                    @Param("status") String status,
                                    @Param("instanceId") Integer instanceId);
    //插入交易记录表（类型是求购）
    void insertPurchaseRequestTransaction(
            @Param("nftId") int nftId,
            @Param("fromAddress") String fromAddress,
            @Param("toAddress") String toAddress,
            @Param("transactionHash") String transactionHash,
            @Param("price") BigDecimal price,
            @Param("currency") String currency,
            @Param("type") String type,
            @Param("purchaseRequestId") Integer purchaseRequestId,
            @Param("instanceId") Integer instanceId
    );
}