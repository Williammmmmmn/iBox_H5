package com.joon.ibox_back_end.personalHub.controller;

import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.personalHub.entity.UserAssetsResponseDto;
import com.joon.ibox_back_end.personalHub.entity.cancelSale.CancelConsignmentRequest;
import com.joon.ibox_back_end.personalHub.service.CancleSaleService;
import com.joon.ibox_back_end.personalHub.service.UserAssetService;
import com.joon.ibox_back_end.wallet.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private CancleSaleService cancleSaleService;

    /**
     * 查询个人藏品资产
     * @param walletAddress
     * @return
     */
    @GetMapping("/assets")
    public R getUserAssets(String walletAddress) {
        UserAssetsResponseDto userAssets = userAssetService.getUserAssets(walletAddress);
        return R.success("查询个人藏品资产成功",userAssets);
    }

    @PostMapping("/cancelSale")
    public R cancelSale(@RequestBody CancelConsignmentRequest request) {
        try {
            cancleSaleService.cancelConsignment(
                    request.getWalletAddress(),
                    request.getInstanceId()
            );
            return R.success("取消寄售成功");
        } catch (BusinessException e) {
            return R.error(400, e.getMessage());
        }catch (Exception e) {
            return R.error(500, "取消寄售失败，请稍后重试");
        }
    }

}
