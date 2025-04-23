package com.joon.ibox_back_end.personalHub.service;

import com.joon.ibox_back_end.personalHub.entity.orders.OrderDto;
import com.joon.ibox_back_end.personalHub.entity.orders.OrderPaymentInfoDTO;

import java.util.List;

public interface OrderService {
    List<OrderDto> getPendingPaymentOrders(String walletAddress);
    List<OrderDto> getRequestingOrders(String walletAddress);
    List<OrderDto> getCompletedOrders(String walletAddress);
    List<OrderDto> getAllOrders(String walletAddress);


    /**
     * 获取待支付订单关键信息
     * @param orderId
     * @return
     */
    OrderPaymentInfoDTO getOrderPaymentInfo(String orderId);
}
