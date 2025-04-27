 import request from '@/utils/request';
 /**
 * 锁定NFT实例

 */
 export const lockNFTInstance = async (walletAddress, instanceId) => {
    try {
      const response = await request.post('/buyLock/lock', {
        walletAddress,
        instanceId
      });
      return response.data;
    } catch (error) {
      console.error('锁定NFT实例失败:', error);
      throw error;
    }
  };

  //取消待支付订单
export const cancelOrder = async(instanceId) => {
  try{
  const response = request.post(`/buyLock/unlock/${instanceId}`)
  return response;
  } catch (error) {
    console.error('取消待支付订单:', error);
    throw error;
  }
};
