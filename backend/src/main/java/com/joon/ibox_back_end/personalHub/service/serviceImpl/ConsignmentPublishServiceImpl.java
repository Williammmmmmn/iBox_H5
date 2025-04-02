package com.joon.ibox_back_end.personalHub.service.serviceImpl;

import com.joon.ibox_back_end.commonEntity.po.Instances;
import com.joon.ibox_back_end.personalHub.entity.ConsignmentPublishDto;
import com.joon.ibox_back_end.personalHub.mapper.ConsignmentPublishMapper;
import com.joon.ibox_back_end.personalHub.service.ConsignmentPublishService;
import com.joon.ibox_back_end.wallet.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 寄售发布实现类
 * @author: Joonnn
 * @create: 2025-04-0216:45
 **/
@Service
public class ConsignmentPublishServiceImpl implements ConsignmentPublishService {
    @Autowired
    private ConsignmentPublishMapper consignmentPublishMapper;
    @Override
    @Transactional
    public void createConsignment(Integer instanceId, String sellerAddress,
                                  BigDecimal price, String currency) {
        // 1. 验证NFT实例
        Instances instance = consignmentPublishMapper.selectById(instanceId);
        if (instance == null) {
            throw new BusinessException("NFT实例不存在");
        }
        if (!sellerAddress.equals(instance.getOwnerAddress())) {
            throw new BusinessException("您不是该NFT的拥有者");
        }
        if (instance.getIsConsigning()) {
            throw new BusinessException("该NFT已在寄售中");
        }

        // 2. 更新NFT实例状态
        instance.setIsConsigning(true);
        instance.setPrice(price);
        consignmentPublishMapper.updateConsignmentStatus(instance);

        // 3. 创建寄售记录
        ConsignmentPublishDto consignment = new ConsignmentPublishDto();
        consignment.setInstanceId(instanceId);
        consignment.setSellerAddress(sellerAddress);
        consignment.setCurrency(currency);
        consignment.setStatus("active");
        consignment.setIsActive(true);

        consignmentPublishMapper.insert(consignment);
    }
}
