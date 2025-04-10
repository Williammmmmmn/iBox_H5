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
 
// 获取 NFT 寄售列表数据
export const getNFTDetail = async (id) => {
  try {
    const response = await request.get(`/nfts/detail/${id}`); // 根据 NFT ID 获取详情
    return response.data;
  } catch (error) {
    console.error('获取 NFT 寄售列表失败:', error);
    throw error;
  }
};
// 获取 NFT 求购列表数据
export const getPurchaseRequestsByNftId = async (id) => {
  try {
    const response = await request.get(`/nfts/bid/${id}`); // 根据 NFT ID 获取详情
    return response.data;
  } catch (error) {
    console.error('获取 NFT 求购列表失败:', error);
    throw error;
  }
};
// 获取藏品公告列表
export const getAnnounceList = async (nftId) => {
  try {
    const response = await request.get(`/nfts/list/${nftId}`);
    return response.data;
  } catch (error) {
    console.error('获取 公告列表失败:', error);
    throw error;
  }
};
// 获取藏品公告详情
export const getAnnounceDetail  = async (announceId) => {
  try {
    const response = await request.get(`/nfts/announceDetail/${announceId}`);
    return response.data;
  } catch (error) {
    console.error('获取 公告列表失败:', error);
    throw error;
  }
};
// 获取 藏品寄售详情
export const getSaleDetail = async (nftId, instanceNumber) => {
  try {
    const response = await request.get(`/nfts/sale/${nftId}?instanceNumber=${instanceNumber}`);
    return response.data;
  } catch (error) {
    console.error('获取 藏品寄售信息失败:', error);
    throw error;
  }
};
// 购买 API
export const purchaseItem = async (walletAddress, instanceId, price) => {
  try {
    const response = await request.post('/nfts/purchase', {
      walletAddress,
      instanceId,
      price
    });
    return response.data; // 返回响应数据
  } catch (error) {
    console.error('购买失败:', error);
    throw error; // 抛出错误以便调用方处理
  }
};



 
