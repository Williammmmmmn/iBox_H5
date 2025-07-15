package com.joon.ibox_back_end.market.mapper;

import com.joon.ibox_back_end.market.dto.NftListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NftListMapper {
    List<NftListDto> getNFTListByTag(String tag);
}
