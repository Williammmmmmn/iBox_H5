package com.joon.ibox_back_end.saleRecord.service;

public interface SaleRecordService {
    Object getOfficialSales(String category, String status, String sort, String order);
}
