package com.joon.ibox_back_end.personalHub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: backend
 * @description: 用户资产最终响应
 * @author: Joonnn
 * @create: 2025-04-0101:32
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAssetsResponseDto {
    private UserProfileDto userProfile;
    private List<NftAssetDto> assets;
}
