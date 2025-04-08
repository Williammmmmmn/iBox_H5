package com.joon.ibox_back_end.market.service.serviceImpl;

import com.joon.ibox_back_end.market.entity.*;
import com.joon.ibox_back_end.market.mapper.*;
import com.joon.ibox_back_end.market.service.MarketService;
import com.joon.ibox_back_end.wallet.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
    private SaleDetailMapper saleDetailMapper; ;
    @Autowired
    private AnnounceMapper announceMapper;
    @Autowired
    private PurchaseMapper purchaseMapper;
    /**
     * 查询市场列表
     * @param tag
     * @return
     */
    @Override
    public List<NftListDto> getNFTListByTag(String tag) {
        return nftListMapper.getNFTListByTag(tag);
    }
    /**
     * 查询NFT寄售详情
     * @param nftId
     * @return
     */
    @Override
    public NftSaleDto getNFTSaleInfo(int nftId) {
        return nftSaleInfoMapper.getNFTSaleInfo(nftId);
    }

    /**
     * 查询nft求购信息
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
        return  saleDetailMapper.selectSaleDetail(nftId, instanceNumber);
    }

    /**
     * 查询寄售公告列表
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
     * @param announceId
     * @return
     */
    @Override
    public AnnounceDetailDto selectAnnounceDetailById(Integer announceId) {
        return announceMapper.selectAnnounceDetailById(announceId);
    }

    /**
     * 用户购买另一个用户寄售藏品
     * @param instanceId
     * @param buyerAddress
     * @param price
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
        // 查询寄售者地址
        String sellerWalletAddress  = purchaseMapper.getSellerAddress(instanceId);

        // 查询 NftId
        int nftId = purchaseMapper.getNftIdByInstanceId(instanceId);

        // 生成交易哈希
        String transactionHash = generateTransactionHash();

        // 更新 consignments 表
        purchaseMapper.updateConsignmentStatus(instanceId, buyerWalletAddress);

        // 更新 nft_instances 表
        purchaseMapper.updateNftInstanceOwner(instanceId, buyerWalletAddress);

        // 计算寄售者实际到账金额（扣除 5% 手续费）
        BigDecimal feeRate = new BigDecimal("0.05"); // 手续费率
        BigDecimal sellerAmount = price.multiply(BigDecimal.ONE.subtract(feeRate)); // 计算实际到账金额
        String currency = "CNY"; // 货币类型
        // 插入交易记录
        purchaseMapper.insertTransaction(nftId, sellerWalletAddress, buyerWalletAddress, transactionHash, price,currency, instanceId);

        // 更新购买者钱包余额
        purchaseMapper.updateBuyerWallet(buyerWalletAddress, sellerAmount);

        // 更新寄售者钱包余额
        purchaseMapper.updateSellerWallet(sellerWalletAddress, sellerAmount);
    }

    private String generateTransactionHash() {
        // 生成随机的交易哈希
        return "0x" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
