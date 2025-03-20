package com.joon.ibox_back_end.market.service;

import com.joon.ibox_back_end.market.entity.NftListDto;

import java.util.List;

public interface MarketService {
    /**
     * 查询市场列表
     */
    List<NftListDto> getNFTListByTag(String tag);



}
