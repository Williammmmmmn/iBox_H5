package com.joon.ibox_back_end.personalHub.mapper;

import com.joon.ibox_back_end.commonEntity.po.Instances;
import com.joon.ibox_back_end.personalHub.entity.ConsignmentPublishDto;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ConsignmentPublishMapper {

    @Update("UPDATE nft_instances SET is_consigning = #{isConsigning}, price = #{price} WHERE instance_id = #{instanceId}")
    int updateConsignmentStatus(Instances instance);

    @Select("SELECT * FROM nft_instances WHERE instance_id = #{instanceId}")
    Instances selectById(Integer instanceId);

    @Insert("INSERT INTO consignments(instance_id, seller_address, currency, status, is_active) " +
            "VALUES(#{instanceId}, #{sellerAddress}, #{currency}, #{status}, #{isActive})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ConsignmentPublishDto consignment);
}
