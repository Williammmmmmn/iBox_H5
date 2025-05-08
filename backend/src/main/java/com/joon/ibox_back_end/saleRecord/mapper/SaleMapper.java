package com.joon.ibox_back_end.saleRecord.mapper;

import com.joon.ibox_back_end.saleRecord.entity.SaleRecordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SaleMapper {
    List<SaleRecordDTO> selectOfficialSales(@Param("category") String category,
                                            @Param("status") String status,
                                            @Param("sort") String sort,
                                            @Param("order") String order);
}
