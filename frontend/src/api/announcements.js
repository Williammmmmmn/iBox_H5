import request from '@/utils/request';

// 获取公告列表（带分页和搜索）
export const getAnnouncements = (keyword = '', pageNum = 1, pageSize = 10) => {
  return request.get('/announcements/list', {
    params: {
      keyword,
      pageNum,
      pageSize
    }
  });
};