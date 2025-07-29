import request from '@/utils/request';

//收藏藏品
export const toFavorite = (data) => {
  return request.post('/favorite/toggle', data);
};
// 获取用户收藏列表
export const getFavoriteList = (userId) => {
  return request.get('/favorite/list', { 
    params: { userId } 
  });
};