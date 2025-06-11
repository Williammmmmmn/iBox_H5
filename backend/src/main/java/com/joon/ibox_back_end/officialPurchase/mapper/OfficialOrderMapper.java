package com.joon.ibox_back_end.officialPurchase.mapper;

import com.joon.ibox_back_end.commonEntity.po.OfficialOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface OfficialOrderMapper {
    int createOrder(OfficialOrder order);
    int lockOrder(@Param("orderId") String orderId, @Param("expiry") Date expiry);
    int completeOrder(@Param("orderId") String orderId);
    int cancelOrder(@Param("orderId") String orderId);
    OfficialOrder getOrderById(@Param("orderId") String orderId);
    List<OfficialOrder> getExpiredOrders(@Param("now") Date now);
}
