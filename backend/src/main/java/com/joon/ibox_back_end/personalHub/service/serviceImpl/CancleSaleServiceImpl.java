package com.joon.ibox_back_end.personalHub.service.serviceImpl;

import com.joon.ibox_back_end.personalHub.mapper.CancleSaleMapper;
import com.joon.ibox_back_end.personalHub.service.CancleSaleService;
import com.joon.ibox_back_end.wallet.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: backend
 * @description: 取消寄售实现类
 * @author: Joonnn
 * @create: 2025-04-0317:06
 **/
@Service
public class CancleSaleServiceImpl implements CancleSaleService {
    @Autowired
    private CancleSaleMapper cancleSaleMapper;

    @Override
    @Transactional

    public void cancelConsignment(String walletAddress, Integer instanceId) {
        // 1. 取消寄售记录
        int updated = cancleSaleMapper.cancelConsignment(walletAddress, instanceId);
        if (updated == 0) {
            throw new BusinessException("没有找到可取消的寄售记录");
        }

        // 2. 更新NFT实例状态
        cancleSaleMapper.updateNftInstanceStatus(instanceId);

        // 3. 可以在这里添加交易记录等后续操作
    }


}
