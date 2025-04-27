package com.joon.ibox_back_end.personalHub.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.personalHub.entity.orders.OrderDto;
import com.joon.ibox_back_end.personalHub.entity.orders.OrderPaymentInfoDTO;
import com.joon.ibox_back_end.personalHub.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: backend
 * @description: 我的订单
 * @author: Joonnn
 * @create: 2025-04-2210:33
 **/
@RestController
@RequestMapping("/orders")
public class MyOrdersController {
    private final OrderService orderService;

    public MyOrdersController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/myOrders")
    public R showOrders(
            @RequestParam String walletAddress,
            @RequestParam(required = false, defaultValue = "all") String type,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
            ) {
        PageHelper.startPage(pageNum, pageSize);
        try {
            switch (type) {
                case "pending":
                    List<OrderDto> pendingPaymentOrders = orderService.getPendingPaymentOrders(walletAddress);
                    return R.success("查询待支付订单成功", new PageInfo<>(pendingPaymentOrders));
                case "requesting":
                    List<OrderDto> requestingOrders = orderService.getRequestingOrders(walletAddress);
                    return R.success("查询求购中订单成功", new PageInfo<>(requestingOrders));
                case "completed":
                    List<OrderDto> completedOrders = orderService.getCompletedOrders(walletAddress);
                    return R.success("查询已完成订单成功", new PageInfo<>(completedOrders));
                default:
                    List<OrderDto> allOrders = orderService.getAllOrders(walletAddress);
                    return R.success("查询所有订单成功", new PageInfo<>(allOrders));
            }
        } finally {
            // 确保清除分页参数
            PageHelper.clearPage();
        }
    }



    /**
     * 获取待支付订单的支付信息
     *
     * @param orderId 订单ID
     * @return 支付信息
     */
    @GetMapping("/paymentInfo")
    public R getPaymentInfo(@RequestParam String orderId) {
        try {
            OrderPaymentInfoDTO paymentInfo = orderService.getOrderPaymentInfo(orderId);
            return R.success(paymentInfo);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}
