//市场部分的api
import request from '@/utils/request';

//市场列表查询// 获取 NFT 列表（支持根据 tag 查询）
export const getNFTList = async (tag = null) => {
    try {
      const params = {};
      if (tag) {
        params.tag = tag; // 如果传入了 tag，则添加到查询参数中
      }
      const response = await request.get('/nfts/list', { params });
      return response.data;
    } catch (error) {
      console.error('获取藏品列表失败:', error);
      throw error;
    }
  };
 
// 获取 NFT 详情数据
export const getNFTDetail = async (id) => {
  try {
    const response = await request.get(`/nfts/detail/${id}`); // 根据 NFT ID 获取详情
    return response.data;
  } catch (error) {
    console.error('获取 NFT 详情失败:', error);
    throw error;
  }
};