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