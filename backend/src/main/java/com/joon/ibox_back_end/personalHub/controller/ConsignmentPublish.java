package com.joon.ibox_back_end.personalHub.controller;

import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.personalHub.entity.ConsignmentPublishRequest;
import com.joon.ibox_back_end.personalHub.service.ConsignmentPublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 发布寄售
 * @author: Joonnn
 * @create: 2025-04-0216:35
 **/
@RestController
@RequestMapping("/consignments")
public class ConsignmentPublish {
    @Autowired
    private ConsignmentPublishService consignmentPublishService;



    @PostMapping("/publishSale")
    public R createConsignment(@RequestBody ConsignmentPublishRequest request) {
        consignmentPublishService.createConsignment(
                request.getInstanceId(),
                request.getSellerAddress(),
                request.getPrice(),
                request.getCurrency()
        );
        return R.success("寄售成功");
    }
}
