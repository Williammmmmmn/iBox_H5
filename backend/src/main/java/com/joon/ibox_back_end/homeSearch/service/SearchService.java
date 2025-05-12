package com.joon.ibox_back_end.homeSearch.service;

import com.joon.ibox_back_end.homeSearch.entity.SearchResultDTO;

import java.util.List;

public interface SearchService {
    List<SearchResultDTO> searchNFTs(String keyword);
}
