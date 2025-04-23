package com.joon.ibox_back_end.personalHub.mapper;

import com.joon.ibox_back_end.commonEntity.po.Instances;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface OrderPaymentInfoMapper {

    Instances selectByInstanceId(@Param("instanceId") Integer instanceId);
}
