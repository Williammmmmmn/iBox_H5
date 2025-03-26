package com.joon.ibox_back_end.market.service;

import com.joon.ibox_back_end.market.entity.NftListDto;
import com.joon.ibox_back_end.market.entity.NftSaleDetaiDto;
import com.joon.ibox_back_end.market.entity.NftSaleDto;
import com.joon.ibox_back_end.market.entity.PurchaseRequestDto;

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
     * @param instanceId
     * @return
     */
    NftSaleDetaiDto getSaleDetail(Integer nftId, Integer instanceId);

}
