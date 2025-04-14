package com.joon.ibox_back_end.personalHub.service;

import com.joon.ibox_back_end.personalHub.entity.UserAssetsResponseDto;
import com.joon.ibox_back_end.personalHub.entity.UserSoldAsset;

import java.util.List;


public interface UserAssetService {
    UserAssetsResponseDto getUserAssets(String walletAddress);

    List<UserSoldAsset> getUserSoldAssets(String walletAddress);
}
