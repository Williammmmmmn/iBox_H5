//“我的”部分的api
import request from '@/utils/request';
import store from '@/store'; // 引入store
// 获取用户资产数据
export const getUserAssets = async () => {
    try {
      // 从Vuex获取钱包地址
      const walletAddress = store.getters.getUserInfo?.walletAddress;
      if (!walletAddress) {
        throw new Error('未获取到钱包地址');
      }
      
      const response = await request.get('/personal/assets', {
        params: { walletAddress: walletAddress }
      });
      return response.data;
    } catch (error) {
      console.error('获取用户资产失败:', error);
      throw error;
    }
  };

  //钱包详情信息
  export const getWalletInfo = async () => {
    try {
      // 从Vuex获取钱包地址
      const walletAddress = store.getters.getUserInfo?.walletAddress;
      if (!walletAddress) {
        throw new Error('未获取到钱包地址');
      }
      
      const response = await request.get('/personal/wallet', {
        params: { walletAddress: walletAddress }
      });
      return response.data;
    } catch (error) {
      console.error('获取用户钱包信息失败:', error);
      throw error;
    }
  };