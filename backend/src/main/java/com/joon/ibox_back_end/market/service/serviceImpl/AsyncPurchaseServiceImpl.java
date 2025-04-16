package com.joon.ibox_back_end.market.service.serviceImpl;

import com.joon.ibox_back_end.market.service.AsyncPurchaseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @program: backend
 * @description: 购买后操作异步处理实现类
 * @author: Joonnn
 * @create: 2025-04-1623:56
 **/
@Service
public class AsyncPurchaseServiceImpl implements AsyncPurchaseService {
    @Override
    public void logPurchaseSuccess(String buyerWalletAddress, BigDecimal sellerAmount) {
        // 模拟耗时操作（如发送邮件、发短信、记录日志等）
        try {
            Thread.sleep(1000);  // 模拟1秒延迟
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // 打印购买信息
        System.out.println("【购买成功】买家钱包地址: " + buyerWalletAddress+",【卖家收入】实际到账金额: " + sellerAmount + " CNY");
    }
}
