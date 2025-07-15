package com.joon.ibox_back_end.officialPurchase.service.Impl;

import com.joon.ibox_back_end.commonEntity.po.Instances;
import com.joon.ibox_back_end.commonEntity.po.OfficialOrder;
import com.joon.ibox_back_end.commonEntity.po.OfficialSale;
import com.joon.ibox_back_end.commonEntity.po.Transactions;
import com.joon.ibox_back_end.officialPurchase.entity.OrderRequest;
import com.joon.ibox_back_end.officialPurchase.mapper.InstanceMapper;
import com.joon.ibox_back_end.officialPurchase.mapper.OfficialOrderMapper;
import com.joon.ibox_back_end.officialPurchase.mapper.OfficialSaleMapper;
import com.joon.ibox_back_end.officialPurchase.service.OfficialOrderService;
import com.joon.ibox_back_end.wallet.exception.BusinessException;
import com.joon.ibox_back_end.wallet.mapper.TransactionMapper;
import com.joon.ibox_back_end.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;


@Service
@RequiredArgsConstructor
@Slf4j
public class OfficialOrderServiceImpl implements OfficialOrderService {
    private final OfficialOrderMapper orderMapper;
    private final InstanceMapper instanceMapper;
    private final TransactionMapper transactionMapper;
    private final OfficialSaleMapper saleMapper;
    private final WalletService walletService;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String STOCK_KEY_PREFIX = "nft:stock:";
    private static final long ORDER_LOCK_TIME = 180000; // 3分钟

    @PostConstruct
    public void initStock() {
        List<OfficialSale> sales = saleMapper.getAllSales();
        for (OfficialSale sale : sales) {
            String key = STOCK_KEY_PREFIX + sale.getNftId();
            redisTemplate.opsForValue().set(
                    key,
                    String.valueOf(sale.getIssueCount() - sale.getCirculateCount())
            );
            log.info("初始化NFT库存: nftId={}, 可用库存={}", sale.getNftId(),
                    sale.getIssueCount() - sale.getCirculateCount());
        }
    }
    @Override
    public String createOrder(OrderRequest request) {
        // 1. 检查库存
        int remain = checkStock(request.getNftId(), request.getQuantity());
        if (remain < 0) {
            throw new BusinessException("库存不足");
        }

        // 2. 扣减钱包余额
        if (!walletService.deductBalance(request.getUserAddress(),
                request.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))) {
            throw new BusinessException("余额不足");
        }

        // 3. 生成订单
        String orderId = generateOrderId();
        OfficialOrder order = new OfficialOrder();
        order.setOrderId(orderId);
        order.setNftId(request.getNftId());
        order.setUserAddress(request.getUserAddress());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setStatus("pending");
        order.setLockExpiry(new Date(System.currentTimeMillis() + ORDER_LOCK_TIME));

        orderMapper.createOrder(order);

        // 4. 锁定库存
        lockStock(request.getNftId(), request.getQuantity());

        return orderId;
    }

    @Override
    @Transactional
    public boolean completeOrder(String orderId) {
        try {
            // 1. 获取订单
            OfficialOrder order = orderMapper.getOrderById(orderId);
            if (order == null) {
                throw new BusinessException("订单不存在");
            }

            // 2. 检查订单状态
            if (!"pending".equals(order.getStatus())) {
                throw new BusinessException("订单状态异常");
            }

            // 3. 完成订单
            orderMapper.completeOrder(orderId);

            // 4. 生成NFT实例
            Integer currentMaxNumber = instanceMapper.getMaxInstanceNumber(order.getNftId()) == null ? 0 :
                    instanceMapper.getMaxInstanceNumber(order.getNftId());

            List<Instances> instances = new ArrayList<>();
            for (int i = 1; i <= order.getQuantity(); i++) {
                Instances instance = new Instances();
                instance.setNftId(order.getNftId());
                instance.setOwnerAddress(order.getUserAddress());
                instance.setPrice(order.getPrice());
                instance.setInstanceNumber(currentMaxNumber + i);
                instances.add(instance);
            }

            // 先批量保存实例，获取生成的ID
            instanceMapper.batchCreate(instances);

            // 然后创建交易记录
            List<Transactions> transactions = new ArrayList<>();
            for (Instances instance : instances) {
                Transactions tx = new Transactions();
                tx.setNftId(order.getNftId());
                tx.setToAddress(order.getUserAddress());
                tx.setPrice(order.getPrice());
                tx.setType("purchase");
                tx.setOrderId(order.getId());
                tx.setInstanceId(instance.getInstanceId()); // 使用已保存实例的ID
                tx.setTransactionDate(LocalDateTime.now());
                tx.setTransactionHash("0x" + UUID.randomUUID().toString().replace("-", ""));
                tx.setCurrency("CNY");
                transactions.add(tx);
            }

            transactionMapper.batchCreate(transactions);

            // 5. 更新流通量
            saleMapper.increaseCirculateCount(order.getNftId(), order.getQuantity());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Scheduled(fixedRate = 60000)
    public void processExpiredOrders() {
        List<OfficialOrder> orders = orderMapper.getExpiredOrders(new Date());
        for (OfficialOrder order : orders) {
            try {
                // 1. 取消订单
                orderMapper.cancelOrder(order.getOrderId());

                // 2. 返还库存
                returnStock(order.getNftId(), order.getQuantity());

                // 3. 返还余额
                walletService.refundBalance(order.getUserAddress(),
                        order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));
            } catch (Exception e) {
                log.error("处理过期订单失败: {}", order.getOrderId(), e);
            }
        }
    }

    private String generateOrderId() {
        return "ORD" + System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    private int checkStock(Integer nftId, Integer quantity) {
        String key = STOCK_KEY_PREFIX + nftId;
        Long stock = redisTemplate.opsForValue().decrement(key, quantity);
        if (stock == null || stock < 0) {
            if (stock != null) {
                redisTemplate.opsForValue().increment(key, quantity);
            }
            return -1;
        }
        redisTemplate.opsForValue().increment(key, quantity); // 先返还，实际扣减在锁定后
        return stock.intValue();
    }

    private void lockStock(Integer nftId, Integer quantity) {
        String key = STOCK_KEY_PREFIX + nftId;
        redisTemplate.opsForValue().decrement(key, quantity);
    }

    private void returnStock(Integer nftId, Integer quantity) {
        String key = STOCK_KEY_PREFIX + nftId;
        redisTemplate.opsForValue().increment(key, quantity);
    }
}