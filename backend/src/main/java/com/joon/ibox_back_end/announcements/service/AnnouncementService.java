package com.joon.ibox_back_end.announcements.service;

import com.joon.ibox_back_end.announcements.entity.AnnouncementDto;
import com.joon.ibox_back_end.announcements.entity.AnnouncementQueryDto;
import com.joon.ibox_back_end.common.PageResult;

public interface AnnouncementService {
    PageResult<AnnouncementDto> getAnnouncements(AnnouncementQueryDto queryDTO);
}
