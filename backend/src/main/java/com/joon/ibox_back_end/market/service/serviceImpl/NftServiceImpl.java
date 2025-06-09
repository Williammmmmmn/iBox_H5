package com.joon.ibox_back_end.market.service.serviceImpl;

import com.joon.ibox_back_end.commonEntity.po.Instances;
import com.joon.ibox_back_end.commonEntity.po.PurchaseRequests;
import com.joon.ibox_back_end.market.entity.*;
import com.joon.ibox_back_end.market.mapper.*;
import com.joon.ibox_back_end.market.service.AsyncPurchaseService;
import com.joon.ibox_back_end.market.service.MarketService;
import com.joon.ibox_back_end.wallet.exception.BusinessException;
import com.joon.ibox_back_end.wallet.mapper.WalletMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @program: backend
 * @description: 市场
 * @author: Joonnn
 * @create: 2025-03-2016:59
 **/
@Service
@Slf4j
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

    @Autowired
    private AsyncPurchaseService asyncPurchaseService;  // 注入异步服务

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
        if (nftSaleInfo != null && nftSaleInfo.getInstances() == null) {
            nftSaleInfo.setInstances(Collections.emptyList());
        }
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
    public AnnounceWithDetailDto selectAnnounceDetailById(Integer announceId) {
        return announceMapper.selectAnnounceDetailById(announceId);
    }

    /**
     * 用户购买另一个用户寄售藏品
     *
     * @param instanceId
     * @param price
     */
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
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
        boolean locked = false;
        try {
            // 2.1尝试获取锁（等待1秒，锁定10秒）
            locked = lock.tryLock(1, 10, TimeUnit.SECONDS);
            if (!locked) {
                throw new RuntimeException("当前购买人数较多，请稍后重试");
            }
            //接收卖出到账金额
            BigDecimal sellerAmount = doPurchase(instanceId, buyerWalletAddress, price);
            // 异步记录购买成功信息（不阻塞主流程）
            asyncPurchaseService.logPurchaseSuccess(buyerWalletAddress, sellerAmount);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("操作被中断");
        } finally {
            // 优化点3：安全的锁释放（添加状态检查）
            if (locked && lock.isHeldByCurrentThread()) {
                try {
                    lock.unlock();
                } catch (IllegalMonitorStateException e) {
                    log.warn("锁释放异常（可能已自动过期）", e);
                }
            }
        }
    }

    // 抽取的核心购买方法
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal doPurchase(Integer instanceId, String buyerWalletAddress, BigDecimal price) {
        Instances instance = purchaseMapper.getNftInstanceWithLock(instanceId);
        if (instance == null) {
            throw new IllegalArgumentException("NFT实例不存在");
        }
        // 检查是否已售出（双重检查）
        if (!instance.getIsConsigning()) {
            throw new IllegalArgumentException("该藏品已下架或已售出");
        }
        // 检查购买者是否是卖家本人
        if (buyerWalletAddress.equalsIgnoreCase(instance.getOwnerAddress())) {
            throw new IllegalArgumentException("不能购买自己上架的藏品");
        }
        // 在processPurchase方法中添加余额检查
        BigDecimal buyerBalance = purchaseMapper.getWalletBalance(buyerWalletAddress);
        if (buyerBalance.compareTo(price) < 0) {
            throw new IllegalArgumentException("钱包余额不足");
        }
        // 在processPurchase开始时检查交易是否已存在
        if (purchaseMapper.checkTransactionExists(instanceId, instance.getOwnerAddress(), buyerWalletAddress) > 0) {
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
                instance.getVersion()
        );

        if (updated == 0) {
            throw new ConcurrentModificationException("藏品状态已变更，请重试");
        }
        //立即解锁购买的藏品实例
        lockNftInstanceMapper.unlockNftInstance(instanceId);
        // 更新 consignments 表
        purchaseMapper.updateConsignmentStatus(instanceId, buyerWalletAddress);
        // 插入交易记录
        purchaseMapper.insertTransaction(nftId, instance.getOwnerAddress(), buyerWalletAddress, transactionHash, price, currency, instanceId);

        // 更新购买者钱包余额
        purchaseMapper.updateBuyerWallet(buyerWalletAddress, sellerAmount);

        // 更新寄售者钱包余额
        purchaseMapper.updateSellerWallet(instance.getOwnerAddress(), sellerAmount);

        return sellerAmount;
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
        result.put("instances", instances);
        result.put("quantity", instances.size());

        // 查询钱包余额
        BigDecimal balance = walletMapper.selectBalanceByAddress(walletAddress);
        result.put("balance", balance);
        return result;
    }

    /**
     * 用户出售藏品给求购者
     *
     * @param instanceNumber 藏品编号
     * @param walletAddress  出售者的钱包地址
     * @param price          出售价格
     * @param nftId          NFTID
     */
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void sellToPurchaseRequest(Integer instanceNumber, String walletAddress, BigDecimal price, Integer nftId) throws InterruptedException {
        if (instanceNumber == null || instanceNumber <= 0) {
            throw new IllegalArgumentException("无效的藏品编号");
        }
        if (walletAddress == null || walletAddress.isEmpty()) {
            throw new IllegalArgumentException("钱包地址不能为空");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("价格必须大于0");
        }
        if (nftId == null || nftId <= 0) {
            throw new IllegalArgumentException("NFT ID不能为空");
        }
        // 2. 分布式锁
        String lockKey = "sell_request_lock:" + nftId + ":" + instanceNumber;
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;

        try {
            // 2.1尝试获取锁（等待1秒，锁定10秒）
            locked = lock.tryLock(1, 10, TimeUnit.SECONDS);
            if (!locked) {
                throw new RuntimeException("当前操作人数较多，请稍后重试");
            }
            //执行出售逻辑
            doSellToPurchaseRequest(instanceNumber, walletAddress, price, nftId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("操作被中断");
        } finally {
            // 安全的锁释放
            if (locked && lock.isHeldByCurrentThread()) {
                try {
                    lock.unlock();
                } catch (IllegalMonitorStateException e) {
                    log.warn("锁释放异常（可能已自动过期）", e);
                }
            }
        }
    }

    /**
     * 创建求购
     * @param nftId
     * @param buyerAddress
     * @param price
     * @param quantity
     * @return
     */
    @Override
    @Transactional
    public int createPurchaseRequest(Integer nftId, String buyerAddress, BigDecimal price, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("求购数量必须大于0");
        }
        // 计算总金额
        BigDecimal totalAmount = price.multiply(new BigDecimal(quantity));

        // 1. 先扣款
        int updatedRows = purchaseMapper.updateBuyerWallet(buyerAddress, totalAmount);
        if (updatedRows == 0) {
            // 扣款失败，可能是余额不足或钱包不存在
            throw new RuntimeException("扣款失败，请检查钱包余额或钱包地址是否正确");
        }
        // 创建求购记录列表
        List<PurchaseRequests> requests = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            PurchaseRequests request = new PurchaseRequests();
            request.setNftId(nftId);
            request.setBuyerAddress(buyerAddress);
            request.setPrice(price);
            request.setCurrency("CNY");
            request.setStatus("requesting");
            request.setCreatedAt(LocalDateTime.now());
            request.setUpdatedAt(LocalDateTime.now());
            request.setInstanceId(0); // 根据实际情况设置instanceId

            requests.add(request);
        }
        // 根据数量决定插入方式
        if (quantity == 1) {
            return purchaseRequestMapper.insertPurchaseRequest(requests.get(0)) == 1 ? 1 : 0;

        } else {
            return purchaseRequestMapper.batchInsertPurchaseRequests(requests);

        }

    }

    /**
     * 取消求购
     * @param walletAddress
     * @param purchaseId
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPurchaseRequest(String walletAddress, Integer purchaseId) {
        // 1. 查询求购记录
        PurchaseRequests purchaseRequest = purchaseMapper.getPurchaseRequestById(purchaseId);
        if (purchaseRequest == null) {
            throw new IllegalArgumentException("求购记录不存在");
        }
        // 3. 更新求购记录状态
        purchaseMapper.updatePurchaseRequestStatus(purchaseId, "canceled", 0);
        // 4. 更新求购者钱包余额（退款）
        purchaseMapper.updateSellerWallet(walletAddress, purchaseRequest.getPrice());
    }

    /**
     * 刷新最高求购价
     * @param nftId
     */
    @Override
    public int refreshPurchaseRequestPrice(Integer nftId) {
        int maxPrice = purchaseMapper.selectPurchaseRequestPrice(nftId);
        return maxPrice;
    }

    @Transactional(rollbackFor = Exception.class)
    public void doSellToPurchaseRequest(Integer instanceNumber, String sellerWalletAddress, BigDecimal price, Integer nftId) {
        // 1. 获取NFT实例信息（带锁）
        Instances instance = purchaseMapper.getNftInstanceByNumberAndNftIdWithLock(instanceNumber, nftId);
        if (instance == null) {
            throw new IllegalArgumentException("NFT实例不存在");
        }
        // 2. 获取求购记录
        PurchaseRequests purchaseRequest = purchaseMapper.getActivePurchaseRequestByNftIdAndPrice(nftId, price);
        if (purchaseRequest == null) {
            throw new IllegalArgumentException("没有匹配的求购记录");
        }
        // 3. 检查求购者钱包余额
        BigDecimal buyerBalance = purchaseMapper.getWalletBalance(purchaseRequest.getBuyerAddress());
        if (buyerBalance.compareTo(price) < 0) {
            throw new IllegalArgumentException("求购者钱包余额不足");
        }
        // 4. 检查是否已存在交易
        if (purchaseMapper.checkSellTransactionExists(purchaseRequest.getId(),instance.getInstanceId())>0) {
            throw new IllegalArgumentException("请勿重复操作");
        }
        // 5. 计算实际到账金额（扣除5%手续费）
        BigDecimal feeRate = new BigDecimal("0.05");
        BigDecimal sellerAmount = price.multiply(BigDecimal.ONE.subtract(feeRate));
        String currency = "CNY";
        // 6. 更新NFT所有者（带版本检查）
        int updated = purchaseMapper.updateNftInstanceOwnerForPurchaseRequest(
                instance.getInstanceId(),
                purchaseRequest.getBuyerAddress(),
                instance.getVersion()
        );
        if (updated == 0) {
            throw new ConcurrentModificationException("藏品状态已变更，请重试");
        }
        // 7. 更新求购记录状态
        purchaseMapper.updatePurchaseRequestStatus(purchaseRequest.getId(), "completed", instance.getInstanceId());
        // 8. 插入交易记录
        String transactionHash = "0x" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
        purchaseMapper.insertPurchaseRequestTransaction(
                nftId,
                sellerWalletAddress,
                purchaseRequest.getBuyerAddress(),
                transactionHash,
                price,
                currency,
                "purchase_request",
                purchaseRequest.getId(),
                instance.getInstanceId()
        );
        // 11. 更新求购者钱包余额（扣款）
        purchaseMapper.updateBuyerWallet(purchaseRequest.getBuyerAddress(), price);

        // 12. 更新出售者钱包余额（加款）
        purchaseMapper.updateSellerWallet(sellerWalletAddress, sellerAmount);

    }
}