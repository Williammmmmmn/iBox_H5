package com.joon.ibox_back_end.announcements.mapper;

import com.joon.ibox_back_end.commonEntity.po.Announcements;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnnouncementMapper {

    List<Announcements> selectAnnouncements(@Param("keyword") String keyword);
}
