package com.joon.ibox_back_end.market.mapper;

import com.joon.ibox_back_end.market.entity.PurchaseRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//求购
@Mapper

public interface PurchaseRequestMapper {
    List<PurchaseRequestDto> getPurchaseRequestsByNftId(@Param("nftId") Integer nftId);
}
