package com.joon.ibox_back_end.announcements.controller;

import com.joon.ibox_back_end.announcements.entity.AnnouncementDto;
import com.joon.ibox_back_end.announcements.entity.AnnouncementQueryDto;
import com.joon.ibox_back_end.announcements.service.AnnouncementService;
import com.joon.ibox_back_end.common.PageResult;
import com.joon.ibox_back_end.common.R;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: backend
 * @description:
 * @author: Joonnn
 * @create: 2025-05-1918:23
 **/
@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
    @Autowired
    private  AnnouncementService announcementService;;


    @GetMapping("/list")
    public R<PageResult<AnnouncementDto>> getAnnouncements(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        AnnouncementQueryDto queryDTO = new AnnouncementQueryDto();
        queryDTO.setKeyword(keyword);
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);

        PageResult<AnnouncementDto> result = announcementService.getAnnouncements(queryDTO);
        return R.success(result);
    }
}
