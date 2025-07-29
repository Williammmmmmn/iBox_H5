package com.joon.ibox_back_end.market.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFavoriteMapper {
    // 添加或移除收藏
    void toggleFavorite(@Param("userId") Integer userId,
                        @Param("nftId") Integer nftId,
                        @Param("actionType") Integer actionType);

    // 获取用户收藏列表
    List<Integer> getFavoriteNftIds(@Param("userId") Integer userId);
}
