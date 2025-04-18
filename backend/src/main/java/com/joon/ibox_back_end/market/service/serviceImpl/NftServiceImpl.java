package com.joon.ibox_back_end.market.service.serviceImpl;

import com.joon.ibox_back_end.commonEntity.po.Instances;
import com.joon.ibox_back_end.market.entity.*;
import com.joon.ibox_back_end.market.mapper.*;
import com.joon.ibox_back_end.market.service.MarketService;
import com.joon.ibox_back_end.wallet.mapper.WalletMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @program: backend
 * @description: 市场
 * @author: Joonnn
 * @create: 2025-03-2016:59
 **/
@Service
public class NftServiceImpl implements MarketService {
    @Autowired
    private NftListMapper nftListMapper;
    @Autowired
    private NftSaleInfoMapper nftSaleInfoMapper;
    @Autowired
    private PurchaseRequestMapper purchaseRequestMapper;
    @Autowired
    private SaleDetailMapper saleDetailMapper;
    ;
    @Autowired
    private AnnounceMapper announceMapper;
    @Autowired
    private PurchaseMapper purchaseMapper;
    @Autowired
    private LockNftInstanceMapper lockNftInstanceMapper;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private NftInstancesMapper nftInstancesMapper;
    /**
     * 查询市场列表
     *
     * @param tag
     * @return
     */
    @Override
    public List<NftListDto> getNFTListByTag(String tag) {
        return nftListMapper.getNFTListByTag(tag);
    }

    /**
     * 查询NFT寄售详情
     *
     * @param nftId
     * @return
     */
    @Override
    public NftSaleDto getNFTSaleInfo(int nftId) {
        NftSaleDto nftSaleInfo = nftSaleInfoMapper.getNFTSaleInfo(nftId);
        return nftSaleInfo;
    }

    /**
     * 查询nft求购信息
     *
     * @param nftId
     */
    @Override
    public List<PurchaseRequestDto> getPurchaseRequestsByNftId(Integer nftId) {
        // 查询求购记录并按价格降序排列
        return purchaseRequestMapper.getPurchaseRequestsByNftId(nftId);
    }

    /**
     * 查询寄售详情信息
     *
     * @param nftId
     * @param instanceNumber
     * @return
     */
    @Override
    public NftSaleDetaiDto getSaleDetail(Integer nftId, Integer instanceNumber) {
        return saleDetailMapper.selectSaleDetail(nftId, instanceNumber);
    }

    /**
     * 查询寄售公告列表
     *
     * @param nftId
     * @return
     */
    @Override
    public List<NftAnnounceDto> getSimpleAnnounceList(Integer nftId) {
        List<NftAnnounceDto> simpleAnnounceList = announceMapper.getSimpleAnnounceList(nftId);
        return simpleAnnounceList;
    }

    /**
     * 查询藏品公告详情
     *
     * @param announceId
     * @return
     */
    @Override
    public AnnounceDetailDto selectAnnounceDetailById(Integer announceId) {
        return announceMapper.selectAnnounceDetailById(announceId);
    }

    /**
     * 用户购买另一个用户寄售藏品
     *
     * @param instanceId
     * @param price
     */
    @Override
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.SERIALIZABLE)
    public void processPurchase(Integer instanceId, String buyerWalletAddress, BigDecimal price) {
        if (instanceId == null || instanceId <= 0) {
            throw new IllegalArgumentException("无效的实例ID");
        }
        if (buyerWalletAddress == null || buyerWalletAddress.isEmpty()) {
            throw new IllegalArgumentException("买家地址不能为空");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("价格必须大于0");
        }
        // 2. 分布式锁
        String lockKey = "purchase_lock:" + instanceId;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 尝试获取锁（等待5秒，锁定30秒）
            if (!lock.tryLock(0, 60, TimeUnit.SECONDS)) {
                throw new RuntimeException("系统繁忙，请稍后再试");
            }
            // 使用新方法获取NFT实例信息（带行锁）
            Instances instanceInfo = purchaseMapper.getNftInstanceWithLock(instanceId);
            if (instanceInfo == null) {
                throw new IllegalArgumentException("NFT实例不存在");
            }
            // 检查是否已售出（双重检查）
            if (!instanceInfo.getIsConsigning()) {
                throw new IllegalArgumentException("该藏品已下架或已售出");
            }
            // 检查购买者是否是卖家本人
            if (buyerWalletAddress.equalsIgnoreCase(instanceInfo.getOwnerAddress())) {
                throw new IllegalArgumentException("不能购买自己上架的藏品");
            }
            // 在processPurchase方法中添加余额检查
            BigDecimal buyerBalance = purchaseMapper.getWalletBalance(buyerWalletAddress);
            if (buyerBalance.compareTo(price) < 0) {
                throw new IllegalArgumentException("钱包余额不足");
            }
            // 在processPurchase开始时检查交易是否已存在
            if (purchaseMapper.checkTransactionExists(instanceId,instanceInfo.getOwnerAddress(), buyerWalletAddress) > 0) {
                throw new IllegalArgumentException("请勿重复操作");
            }
            // 查询 NftId
            int nftId = purchaseMapper.getNftIdByInstanceId(instanceId);

            // 生成交易哈希
            String transactionHash = "0x" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
            // 计算寄售者实际到账金额（扣除 5% 手续费）
            BigDecimal feeRate = new BigDecimal("0.05"); // 手续费率
            BigDecimal sellerAmount = price.multiply(BigDecimal.ONE.subtract(feeRate)); // 计算实际到账金额
            String currency = "CNY"; // 货币类型

            // 更新NFT所有者（带版本检查）
            int updated = purchaseMapper.updateNftInstanceOwner(
                    instanceId,
                    buyerWalletAddress,
                    instanceInfo.getVersion()
            );

            if (updated == 0) {
                throw new ConcurrentModificationException("藏品状态已变更，请重试");
            }
            //立即解锁购买的藏品实例
            lockNftInstanceMapper.unlockNftInstance(instanceId);
            // 更新 consignments 表
            purchaseMapper.updateConsignmentStatus(instanceId, buyerWalletAddress);
            // 插入交易记录
            purchaseMapper.insertTransaction(nftId, instanceInfo.getOwnerAddress(),buyerWalletAddress, transactionHash, price, currency, instanceId);

            // 更新购买者钱包余额
            purchaseMapper.updateBuyerWallet(buyerWalletAddress, sellerAmount);

            // 更新寄售者钱包余额
            purchaseMapper.updateSellerWallet(instanceInfo.getOwnerAddress(), sellerAmount);

        } catch (InterruptedException e) {
            throw new RuntimeException("操作被中断");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }
    /**
     * 出售给求购者先查询自己拥有的藏品信息
     *
     * @param walletAddress
     * @param nftId
     * @return
     */
    @Override
    public HashMap<String, Object> getOwnedInstances(String walletAddress, Integer nftId) {
        HashMap<String, Object> result = new HashMap<>();
        //查询对应钱包地址的实例
        List<Integer> instances = nftInstancesMapper.selectInstancesByOwnerAndNftId(walletAddress, nftId);
        result.put("instances",instances);
        result.put("quantity",instances.size());

        // 查询钱包余额
        BigDecimal balance = walletMapper.selectBalanceByAddress(walletAddress);
        result.put("balance", balance);
        return result;
    }
}
