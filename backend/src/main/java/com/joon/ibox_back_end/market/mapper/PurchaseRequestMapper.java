package com.joon.ibox_back_end.market.mapper;

import com.joon.ibox_back_end.commonEntity.po.PurchaseRequests;
import com.joon.ibox_back_end.market.dto.PurchaseRequestDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//求购
@Mapper

public interface PurchaseRequestMapper {
    List<PurchaseRequestDto> getPurchaseRequestsByNftId(@Param("nftId") Integer nftId);

    // 单条插入
    @Insert("INSERT INTO purchase_requests (nft_id, buyer_address, price, currency, status, instance_id) " +
            "VALUES (#{nftId}, #{buyerAddress}, #{price}, #{currency}, #{status}, #{instanceId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertPurchaseRequest(PurchaseRequests purchaseRequest);

    // 批量插入
    int batchInsertPurchaseRequests(@Param("list") List<PurchaseRequests> purchaseRequests);
}
