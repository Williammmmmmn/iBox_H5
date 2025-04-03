package com.joon.ibox_back_end.personalHub.service;

import org.springframework.transaction.annotation.Transactional;

public interface CancleSaleService {

    /**
     * 取消寄售
     * @param walletAddress
     * @param instanceId
     */


    @Transactional
    void cancelConsignment(String walletAddress, Integer instanceId);
}
