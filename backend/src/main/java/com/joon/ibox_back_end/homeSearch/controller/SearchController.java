package com.joon.ibox_back_end.homeSearch.controller;

import com.joon.ibox_back_end.common.R;
import com.joon.ibox_back_end.homeSearch.entity.SearchResultDTO;
import com.joon.ibox_back_end.homeSearch.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @program: backend
 * @description: 主页搜索控制
 * @author: Joonnn
 * @create: 2025-05-1223:23
 **/
@RestController
@RequestMapping("/findNft")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public R searchNFTs(@RequestParam String keyword) {
        List<SearchResultDTO> searchResultDTOS = searchService.searchNFTs(keyword);
        return R.success(searchResultDTOS);
    }
}
