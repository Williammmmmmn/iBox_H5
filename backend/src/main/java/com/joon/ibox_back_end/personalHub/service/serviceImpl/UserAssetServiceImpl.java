package com.joon.ibox_back_end.personalHub.service.serviceImpl;

import com.joon.ibox_back_end.personalHub.entity.NftAssetDto;
import com.joon.ibox_back_end.personalHub.entity.UserAssetsResponseDto;
import com.joon.ibox_back_end.personalHub.entity.UserProfileDto;
import com.joon.ibox_back_end.personalHub.mapper.UserAssetMapper;
import com.joon.ibox_back_end.personalHub.service.UserAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-04-0101:51
 **/
@Service
public class UserAssetServiceImpl implements UserAssetService {
    @Autowired
    private UserAssetMapper userAssetMapper;

    public UserAssetsResponseDto getUserAssets(String walletAddress) {
        List<UserAssetsResponseDto> rawData = userAssetMapper.selectUserAssetsByWalletAddress(walletAddress);

        if (rawData.isEmpty()) {
            return new UserAssetsResponseDto(); // 返回空对象
        }
        // 提取用户信息（从第一条记录）
        UserProfileDto profile = rawData.get(0).getUserProfile();

        // 合并所有NFT资产
        List<NftAssetDto> allAssets = new ArrayList<>();
        for (UserAssetsResponseDto dto : rawData) {
            allAssets.addAll(dto.getAssets());
        }

          return new UserAssetsResponseDto(profile, allAssets);
    }
}