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

/** 
* 发起求购请求
* @param {string} walletAddress 用户钱包地址
* @param {string} nftId NFT唯一标识
* @param {number} price 求购价格
* @param {number} quantity 求购数量
* @returns {Promise} 返回请求结果
*/
export const createPurchaseRequest = async (buyerAddress, nftId, price, quantity) => {
 try {
   const response = await request.post('/nfts/createPurchase', {
    buyerAddress,
     nftId,
     price,
     quantity
   });
   return response;
 } catch (error) {
   console.error('发起求购请求失败:', error);
   throw error;
 }
};

//取消求购
export const cancelPurchaseRequest = async (walletAddress, purchaseId) => {
  try {
    const response = await request.post('/nfts/cancelPurchaseRequest', {
      walletAddress,
      purchaseId
    });
    return response; // 返回响应数据
  } catch (error) {
    console.error('取消求购失败:', error);
    throw error; // 抛出错误以便调用方处理
  }
}