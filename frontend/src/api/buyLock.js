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