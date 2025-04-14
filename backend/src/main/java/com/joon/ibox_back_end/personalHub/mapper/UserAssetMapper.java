package com.joon.ibox_back_end.personalHub.mapper;

import com.joon.ibox_back_end.personalHub.entity.UserAssetsResponseDto;
import com.joon.ibox_back_end.personalHub.entity.UserSoldAsset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserAssetMapper {
    /**
     * 查询用户资产
     * @param walletAddress
     * @return
     */
    List<UserAssetsResponseDto> selectUserAssetsByWalletAddress(@Param("walletAddress") String walletAddress);

    /**
     * 查询用户已售
     * @param walletAddress
     * @return
     */
    List<UserSoldAsset> selectUserSoldAssets(String walletAddress);
}
