package com.joon.ibox_back_end.officialPurchase.mapper;

import com.joon.ibox_back_end.commonEntity.po.OfficialSale;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OfficialSaleMapper {
    int getIssueCount(@Param("nftId") Integer nftId);
    int getCirculateCount(@Param("nftId") Integer nftId);
    int increaseCirculateCount(@Param("nftId") Integer nftId, @Param("quantity") Integer quantity);

    List<OfficialSale> getAllSales();
}
