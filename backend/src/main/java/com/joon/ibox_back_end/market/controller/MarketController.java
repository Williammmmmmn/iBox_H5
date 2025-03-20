package com.joon.ibox_back_end.market.controller;

import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.market.entity.NftListDto;
import com.joon.ibox_back_end.market.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
