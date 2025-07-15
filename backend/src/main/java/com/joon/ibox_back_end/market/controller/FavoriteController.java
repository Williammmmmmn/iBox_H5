package com.joon.ibox_back_end.market.controller;

import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.market.dto.FavoriteDto;
import com.joon.ibox_back_end.market.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-07-1510:43
 **/
@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private  FavoriteService favoriteService;


    /**
     * 操作收藏
     * @param favoriteDto
     * @return
     */
    @PostMapping("/toggle")
    public R toggleFavorite(@RequestBody FavoriteDto favoriteDto) {
        try {
            favoriteService.toggleFavorite(
                    favoriteDto.getUserId(),
                    favoriteDto.getNftId(),
                    favoriteDto.getActionType()
            );
            return R.success("操作成功");
        } catch (Exception e) {
            return R.error("收藏失败: " + e.getMessage());
        }
    }

    /**
     * 查询用户收藏列表
     * @param userId
     * @return
     */
    @GetMapping("/list")
    public R getFavoriteList(
            @RequestParam("userId") Integer userId) {
        try {
            List<Integer> favorites = favoriteService.getUserFavorites(userId);
            return R.success("查询收藏",favorites);
        } catch (Exception e) {
            return R.error("收藏失败: " + e.getMessage());
        }
    }

}
