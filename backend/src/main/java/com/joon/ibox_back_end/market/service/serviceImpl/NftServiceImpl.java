package com.joon.ibox_back_end.market.service.serviceImpl;

import com.joon.ibox_back_end.market.entity.NftListDto;
import com.joon.ibox_back_end.market.entity.NftSaleDto;
import com.joon.ibox_back_end.market.mapper.NftListMapper;
import com.joon.ibox_back_end.market.mapper.NftSaleInfoMapper;
import com.joon.ibox_back_end.market.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
