package com.joon.ibox_back_end.personalHub.service;

import com.joon.ibox_back_end.personalHub.entity.UserAssetsResponseDto;


public interface UserAssetService {
    UserAssetsResponseDto getUserAssets(String walletAddress);
}
