package com.joon.ibox_back_end.market.service.serviceImpl;

import com.joon.ibox_back_end.market.mapper.UserFavoriteMapper;
import com.joon.ibox_back_end.market.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-07-1510:49
 **/
@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private  UserFavoriteMapper favoriteMapper;



    public void toggleFavorite(Integer userId, Integer nftId, Integer actionType) {
        if (userId == null || nftId == null || actionType == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        favoriteMapper.toggleFavorite(userId, nftId, actionType);
    }

    public List<Integer> getUserFavorites(Integer userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return favoriteMapper.getFavoriteNftIds(userId);
    }
}
