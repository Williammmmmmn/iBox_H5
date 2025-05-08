package com.joon.ibox_back_end.saleRecord.service.serviceImpl;

import com.joon.ibox_back_end.saleRecord.mapper.SaleMapper;
import com.joon.ibox_back_end.saleRecord.service.SaleRecordService;
import org.springframework.stereotype.Service;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-05-0817:22
 **/
@Service
public class SaleRecordServiceImpl implements SaleRecordService {
    private final SaleMapper saleMapper;

    public SaleRecordServiceImpl(SaleMapper saleMapper) {
        this.saleMapper = saleMapper;
    }


    @Override
    public Object getOfficialSales(String category, String status, String sort, String order) {
        return saleMapper.selectOfficialSales(
                "全部".equals(category) ? null : category,
                "全部".equals(status) ? null : status,
                sort,
                order
        );
    }
}
