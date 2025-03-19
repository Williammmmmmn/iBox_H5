package com.joon.ibox_back_end.market.controller;

import com.joon.ibox_back_end.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: backend
 * @description: 市场控制器
 * @author: Joonnn
 * @create: 2025-03-2001:22
 **/
@RestController
@RequestMapping("/nfts")
public class MarketController {

    @GetMapping("/list")
    public R getNFTList(@RequestParam(required = false) String tag){
        System.out.println(tag);
        return R.success("获取测试");
    }
}
