package com.joon.ibox_back_end.homeSearch.service.serviceImpl;

import com.joon.ibox_back_end.homeSearch.entity.SearchResultDTO;
import com.joon.ibox_back_end.homeSearch.mapper.SearchMapper;
import com.joon.ibox_back_end.homeSearch.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-05-1223:31
 **/
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchMapper searchMapper;

    public List<SearchResultDTO> searchNFTs(String keyword) {
        return searchMapper.searchNFTs(keyword);
    }
}
