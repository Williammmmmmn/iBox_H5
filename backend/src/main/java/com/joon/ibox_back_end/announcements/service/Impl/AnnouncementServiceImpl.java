package com.joon.ibox_back_end.announcements.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joon.ibox_back_end.announcements.entity.AnnouncementDto;
import com.joon.ibox_back_end.announcements.entity.AnnouncementQueryDto;
import com.joon.ibox_back_end.announcements.mapper.AnnouncementMapper;
import com.joon.ibox_back_end.announcements.service.AnnouncementService;
import com.joon.ibox_back_end.common.PageResult;
import com.joon.ibox_back_end.commonEntity.po.Announcements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: backend
 * @description: 查询公告列表实现类
 * @author: Joonnn
 * @create: 2025-05-1918:27
 **/
@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    @Autowired
    private  AnnouncementMapper announcementMapper;
    @Override
    public PageResult<AnnouncementDto> getAnnouncements(AnnouncementQueryDto queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<Announcements> announcements = announcementMapper.selectAnnouncements(
                queryDTO.getKeyword()
        );

        PageInfo<Announcements> pageInfo = new PageInfo<>(announcements);

        List<AnnouncementDto> dtoList = announcements.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        PageResult<AnnouncementDto> result = new PageResult<>();
        result.setList(dtoList);
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());

        return result;

    }

    private AnnouncementDto convertToDTO(Announcements announcements) {
        AnnouncementDto dto = new AnnouncementDto();
        BeanUtils.copyProperties(announcements, dto);
        return dto;
    }
}
