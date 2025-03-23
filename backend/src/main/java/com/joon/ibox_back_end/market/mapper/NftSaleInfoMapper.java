package com.joon.ibox_back_end.market.mapper;

import com.joon.ibox_back_end.market.entity.NftSaleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NftSaleInfoMapper {
    NftSaleDto getNFTSaleInfo(@Param("nftId") int nftId);
}
