package com.joon.ibox_back_end.officialPurchase.controller;

import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.officialPurchase.entity.OrderRequest;
import com.joon.ibox_back_end.officialPurchase.service.OfficialOrderService;
import com.joon.ibox_back_end.wallet.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-06-0914:58
 **/
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OfficialPurchaseController {
    @Autowired
    private OfficialOrderService orderService;

    @PostMapping("/create")
    public R<String> createOrder(@RequestBody OrderRequest request) {
        try {
            String orderId = orderService.createOrder(request);
            return R.success(orderId);
        } catch (BusinessException e) {
            return R.error(e.getMessage());
        }
    }

    @PostMapping("/complete")
    public R<Boolean> completeOrder(@RequestParam String orderId) {
        try {
            boolean result = orderService.completeOrder(orderId);
            return R.success(result);
        } catch (BusinessException e) {
            return R.error(e.getMessage());
        }
    }
}

