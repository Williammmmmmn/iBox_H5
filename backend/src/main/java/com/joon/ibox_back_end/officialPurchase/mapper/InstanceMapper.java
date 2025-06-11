package com.joon.ibox_back_end.officialPurchase.mapper;

import com.joon.ibox_back_end.commonEntity.po.Instances;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InstanceMapper {
    int create(Instances instances);
    Integer getMaxInstanceNumber(@Param("nftId") Integer nftId);
    int batchCreate(@Param("instances") List<Instances> instances);
}
