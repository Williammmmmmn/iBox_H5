package com.joon.ibox_back_end.personalHub.service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface ConsignmentPublishService {
    @Transactional
    void createConsignment(Integer instanceId, String sellerAddress, BigDecimal price, String currency);
}
