package com.joon.ibox_back_end.market.mapper;

import com.joon.ibox_back_end.market.dto.NftSaleDetaiDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SaleDetailMapper {
    NftSaleDetaiDto selectSaleDetail(
            @Param("nftId") Integer nftId,
            @Param("instanceNumber") Integer instanceNumber);
}
