package com.joon.ibox_back_end.market.service;

import com.joon.ibox_back_end.market.entity.NftListDto;
import com.joon.ibox_back_end.market.entity.NftSaleDto;

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

}
