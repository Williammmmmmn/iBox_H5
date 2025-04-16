package com.joon.ibox_back_end.market.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NftInstancesMapper {
    List<Integer> selectInstancesByOwnerAndNftId(@Param("walletAddress") String walletAddress,
                                                 @Param("nftId") Integer nftId);
}
