package com.joon.ibox_back_end.market.service.serviceImpl;

import com.joon.ibox_back_end.market.entity.NftListDto;
import com.joon.ibox_back_end.market.entity.NftSaleDetaiDto;
import com.joon.ibox_back_end.market.entity.NftSaleDto;
import com.joon.ibox_back_end.market.entity.PurchaseRequestDto;
import com.joon.ibox_back_end.market.mapper.NftListMapper;
import com.joon.ibox_back_end.market.mapper.NftSaleInfoMapper;
import com.joon.ibox_back_end.market.mapper.PurchaseRequestMapper;
import com.joon.ibox_back_end.market.mapper.SaleDetailMapper;
import com.joon.ibox_back_end.market.service.MarketService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
     * @param instanceId
     * @return
     */
    @Override
    public NftSaleDetaiDto getSaleDetail(Integer nftId, Integer instanceId) {
        return  saleDetailMapper.selectSaleDetail(nftId, instanceId);
    }
}
