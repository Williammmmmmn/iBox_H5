 import request from '@/utils/request';
 /**
 *出售给求购者查询自己拥有藏品信息

 */
 export const sellPurchaseRequest = async (walletAddress, nftId) => {
    try {
      const response = await request.get('/nfts/sellPurchaseRequest', {
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