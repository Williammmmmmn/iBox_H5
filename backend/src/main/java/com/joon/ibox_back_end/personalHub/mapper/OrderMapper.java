package com.joon.ibox_back_end.personalHub.mapper;

import com.joon.ibox_back_end.commonEntity.po.Instances;
import com.joon.ibox_back_end.personalHub.entity.orders.OrderDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    // 查询待支付订单(锁定但未支付的NFT)
    List<OrderDto> selectPendingPaymentOrders(@Param("walletAddress") String walletAddress);

    // 查询求购中订单
    List<OrderDto> selectRequestingPurchaseRequests(@Param("walletAddress") String walletAddress);

    // 查询已完成订单
    List<OrderDto> selectCompletedTransactions(@Param("walletAddress") String walletAddress);

    // 查询所有订单(待支付+求购中+已完成)
    List<OrderDto> selectAllOrders(@Param("walletAddress") String walletAddress);



    Instances selectByInstanceId(@Param("instanceId") Integer instanceId);
}
