package com.joon.ibox_back_end.market.service;

import com.joon.ibox_back_end.market.entity.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface MarketService {
    /**
     * 查询市场列表
     * @param tag
     * @return
     */
    List<NftListDto> getNFTListByTag(String tag);

    /**
     * 查询NFT寄售详情
     * @param nftId
     * @return
     */
    NftSaleDto getNFTSaleInfo(int nftId);

    /**
     * 查询nft求购信息
     * @param nftId
     */
    List<PurchaseRequestDto> getPurchaseRequestsByNftId(Integer nftId);

    /**
     * 查询寄售详情信息
     *
     * @param nftId
     * @param instanceNumber
     * @return
     */
    NftSaleDetaiDto getSaleDetail(Integer nftId, Integer instanceNumber);

    /**
     * 查询寄售公告列表
     * @param nftId
     * @return
     */
    List<NftAnnounceDto> getSimpleAnnounceList(Integer nftId);

    /**
     * 查询藏品公告详情
     * @param announceId
     * @return
     */
    AnnounceDetailDto selectAnnounceDetailById(Integer announceId);

    /**
     * 用户购买另一个用户寄售藏品
     * @param instanceId
     * @param price
     * @return
     */
    @Transactional
    void processPurchase(Integer instanceId, String buyerWalletAddress, BigDecimal price);



}
