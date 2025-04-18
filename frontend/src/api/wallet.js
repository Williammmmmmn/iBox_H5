import request from '@/utils/request';

// 获取钱包信息
export const getWalletInfo = async (walletAddress) => {
  try {
    const response = await request.get('/wallet/info', {
      params: { walletAddress }
    });
    return response;
  } catch (error) {
    console.error('获取用户钱包信息失败:', error);
    throw error;
  }
};

// 充值钱包
export const rechargeWallet = async (walletAddress, amount) => {
  try {
    const response = await request.post('/wallet/recharge', {
      walletAddress,
      amount
    });
    return response;
  } catch (error) {
    console.error('钱包充值失败:', error);
    throw error;
  }
};

// 提现
export const withdrawWallet = async (walletAddress, amount) => {
  try {
    const response = await request.post('/wallet/withdraw', {
      walletAddress,
      amount
    });
    return response;
  } catch (error) {
    console.error('钱包提现失败:', error);
    throw error;
  }
};

// 获取交易记录
export const getWalletTransactions = async (walletAddress) => {
  try {
    const response = await request.get('/wallet/transactions', {
      params: { walletAddress }
    });
    return response;
  } catch (error) {
    console.error('获取交易记录失败:', error);
    throw error;
  }
};

/**
 * 查询用户钱包余额
 * @param {string} walletAddress 用户钱包地址
 * @returns {Promise<number>} 返回用户余额
 */
export const getUserWalletBalance = async (walletAddress) => {
  try {
    const response = await request.get('/wallet/getBalance', {
      params: {  // 使用params传递GET参数
        walletAddress
      }
    });
    return response.data; // 返回用户余额
  } catch (error) {
    console.error('查询用户钱包余额失败:', error);
    throw error;
  }
};