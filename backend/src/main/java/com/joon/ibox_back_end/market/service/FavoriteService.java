package com.joon.ibox_back_end.market.service;

import java.util.List;

public interface FavoriteService {
    void toggleFavorite(Integer userId, Integer nftId, Integer actionType);

    List<Integer> getUserFavorites(Integer userId);
}
