package com.joon.ibox_back_end.market.mapper;

import com.joon.ibox_back_end.market.entity.AnnounceWithDetailDto;
import com.joon.ibox_back_end.market.entity.NftAnnounceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnnounceMapper {

    /**
     * 获取NFT关联的公告简略列表
     * @param nftId NFT ID
     * @return 公告简略信息列表
     */
    List<NftAnnounceDto> getSimpleAnnounceList(@Param("nftId") Integer nftId);


    /**
     * 查询藏品公告详情
     * @param announceId
     * @return
     */
    AnnounceWithDetailDto selectAnnounceDetailById(@Param("id")Integer announceId);
}
