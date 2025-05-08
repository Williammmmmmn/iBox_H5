package com.joon.ibox_back_end.saleRecord.controller;

import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.saleRecord.service.SaleRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-05-0817:18
 **/
@RestController
@RequestMapping("/official")
@RequiredArgsConstructor
public class SaleController {
    private final SaleRecordService saleRecordService;

    @GetMapping("/saleRecord")
    public R getOfficialSales(@RequestParam(required = false) String category,
                              @RequestParam(required = false) String status,
                              @RequestParam(defaultValue = "time") String sort,
                              @RequestParam(defaultValue = "desc") String order)
    {
        return R.success(saleRecordService.getOfficialSales(category, status, sort, order));
    }
}

