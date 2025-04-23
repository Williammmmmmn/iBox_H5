package com.joon.ibox_back_end.personalHub.service.serviceImpl;

import com.joon.ibox_back_end.commonEntity.po.Instances;
import com.joon.ibox_back_end.personalHub.entity.orders.OrderDto;
import com.joon.ibox_back_end.personalHub.entity.orders.OrderPaymentInfoDTO;
import com.joon.ibox_back_end.personalHub.mapper.OrderMapper;
import com.joon.ibox_back_end.personalHub.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: backend
 * @description: 我的订单实现类
 * @author: Joonnn
 * @create: 2025-04-2210:41
 **/
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<OrderDto> getPendingPaymentOrders(String walletAddress) {
        List<OrderDto> orderDtos = orderMapper.selectPendingPaymentOrders(walletAddress);
        return orderDtos;
    }

    @Override
    public List<OrderDto> getRequestingOrders(String walletAddress) {
        List<OrderDto> orderDtos = orderMapper.selectRequestingPurchaseRequests(walletAddress);
        return orderDtos;
    }

    @Override
    public List<OrderDto> getCompletedOrders(String walletAddress) {
        List<OrderDto> orderDtos = orderMapper.selectCompletedTransactions(walletAddress);
        return orderDtos;
    }

    @Override
    public List<OrderDto> getAllOrders(String walletAddress) {
        List<OrderDto> orderDtos = orderMapper.selectAllOrders(walletAddress);
        return orderDtos;
    }

    @Override
    public OrderPaymentInfoDTO getOrderPaymentInfo(String orderId) {


        // 1. 查询NFT实例信息
        Instances instance = orderMapper.selectByInstanceId(Integer.parseInt(orderId.substring(orderId.indexOf("-")+1)));
        if (instance == null) {
            throw new RuntimeException("订单不存在");
        }

        // 3. 计算剩余时间(秒)
        int remainingSeconds = 0;
        if (instance.getLockExpiry() != null && instance.getLockExpiry().isAfter(LocalDateTime.now())) {
            Duration duration = Duration.between(LocalDateTime.now(), instance.getLockExpiry());
            remainingSeconds = (int) duration.getSeconds();
        }

        // 4. 构建返回DTO
        OrderPaymentInfoDTO dto = new OrderPaymentInfoDTO();
        dto.setNftId(instance.getNftId());
        dto.setInstanceNumber(instance.getInstanceNumber());
        dto.setLockedAt(instance.getLockedAt());
        dto.setLockExpiry(instance.getLockExpiry());
        dto.setRemainingSeconds(remainingSeconds);

        return dto;
    }

    ;
}
