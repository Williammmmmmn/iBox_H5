package com.joon.ibox_back_end.market.controller;

import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.market.entity.*;
import com.joon.ibox_back_end.market.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: backend
 * @description: 市场控制器
 * @author: Joonnn
 * @create: 2025-03-2001:22
 **/
@RestController
@RequestMapping("/nfts")
public class MarketController {
    @Autowired
    private MarketService marketService;
    /**
     * 市场列表
     * @param tag
     * @return
     */
    @GetMapping("/list")
    public R getNFTList(@RequestParam(required = false) String tag){
        // 调用 Service 查询数据
        List<NftListDto> nftList = marketService.getNFTListByTag(tag);
        return R.success("查询市场列表成功",nftList);
    }
    /**
     * 查询NFT寄售详情
     * @param nftId
     * @return
     */
    @GetMapping("/detail/{nftId}")
    public R getNFTDetail(@PathVariable int nftId) {
        NftSaleDto nftSaleInfo = marketService.getNFTSaleInfo(nftId);
        return R.success("查询藏品寄售信息成功",nftSaleInfo);
    }

    /**
     * 查询nft求购信息
     * @param nftId
     * @return
     */
    @GetMapping("/bid/{nftId}")
    public R getPurchaseRequestsByNftId(@PathVariable Integer nftId) {
        List<PurchaseRequestDto> purchaseRequestsByNftId = marketService.getPurchaseRequestsByNftId(nftId);
        return R.success("查询藏品求购信息成功",purchaseRequestsByNftId);
    }

    /**
     * 查询nft寄售详情
     * @param nftId
     * @param instanceNumber
     * @return
     */
    @GetMapping("/sale/{nftId}")
    public R getSaleDetail(
            @PathVariable Integer nftId,
            @RequestParam Integer instanceNumber) {
        NftSaleDetaiDto saleDetail = marketService.getSaleDetail(nftId, instanceNumber);
        return R.success("查询藏品寄售信息成功",saleDetail);
    }

    /**
     * 查询寄售公告列表
     * @param nftId
     * @return
     */
    @GetMapping("/list/{nftId}")
    public R getAnnounceList(@PathVariable Integer nftId) {
        List<NftAnnounceDto> list = marketService.getSimpleAnnounceList(nftId);
        return R.success("查询藏品公告列表成功",list);
    }

    /**
     * 查询寄售公告列表
     * @param announceId
     * @return
     */
    @GetMapping("/announceDetail/{announceId}")
    public R getAnnounceDetail(@PathVariable Integer announceId) {
        AnnounceDetailDto announceDetail = marketService.selectAnnounceDetailById(announceId);
        return R.success("查询藏品公告详情成功",announceDetail);
    }
}
