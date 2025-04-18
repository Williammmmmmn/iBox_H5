 import request from '@/utils/request';
 /**
 *出售给求购者查询自己拥有藏品信息

 */
 export const sellPurchaseRequest = async (walletAddress, nftId) => {
    try {
      const response = await request.get('/nfts/sellPurchaseRequestInfo', {
        params: {  // 使用params传递GET参数
            walletAddress,
            nftId
          }
      });
      return response.data;
    } catch (error) {
      console.error('出售给求购者：查询用户拥有藏品信息失败:', error);
      throw error;
    }
  };

  //射求购API
export const sellToPurchaseRequest = async (walletAddress, instanceNumber, price,nftId) => {
  try {
    const response = await request.post('/nfts/sellToPurchaseRequest', {
      walletAddress,
      instanceNumber,
      price,
      nftId
    });
    return response; // 返回响应数据
  } catch (error) {
    console.error('售出给求购失败:', error);
    throw error; // 抛出错误以便调用方处理
  }
};