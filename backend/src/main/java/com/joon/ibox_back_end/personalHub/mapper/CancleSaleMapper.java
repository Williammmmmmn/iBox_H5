package com.joon.ibox_back_end.personalHub.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CancleSaleMapper {

    // 取消寄售
    int cancelConsignment(@Param("walletAddress") String walletAddress,
                          @Param("instanceId") Integer instanceId);

    // 更新NFT实例状态
    int updateNftInstanceStatus(@Param("instanceId") Integer instanceId);
}
