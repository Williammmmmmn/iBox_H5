package com.joon.ibox_back_end.personalHub.controller;

import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.personalHub.entity.UserAssetsResponseDto;
import com.joon.ibox_back_end.personalHub.service.UserAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: backend
 * @description: 用户资产controller
 * @author: Joonnn
 * @create: 2025-04-0100:47
 **/
@RestController
@RequestMapping("/personal")
public class UserAssetController {

    @Autowired
    private UserAssetService userAssetService;
    @GetMapping("/assets")
    public R getUserAssets(String walletAddress) {
        UserAssetsResponseDto userAssets = userAssetService.getUserAssets(walletAddress);
        return R.success("查询个人藏品资产成功",userAssets);
    }
}
