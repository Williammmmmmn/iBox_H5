package com.joon.ibox_back_end.homeSearch.mapper;

import com.joon.ibox_back_end.homeSearch.entity.SearchResultDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchMapper {

    List<SearchResultDTO> searchNFTs(String keyword);
}
