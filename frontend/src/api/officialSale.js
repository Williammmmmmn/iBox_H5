import request from '@/utils/request';

// 获取发售记录列表
export const getOfficialSales = (params = {}) => {
  // 过滤空值参数
  const filteredParams = Object.entries(params).reduce((acc, [key, value]) => {
    if (value !== '' && value !== undefined && value !== null) {
      acc[key] = value
    }
    return acc
  }, {})

  return request.get('/official/saleRecord', {
    params: filteredParams
  }).then(response => response.data)
}


export const createOrder = async (walletAddress, nftId, price, quantity) => {
  try {
    const response = await request.post('/api/orders/create', {
      userAddress: walletAddress,
      nftId,
      price,
      quantity
    });
    return response.data;
  } catch (error) {
    console.error('创建订单失败:', error);
    throw error;
  }
};

export const completeOrder = async (orderId) => {
  try {
    const response = await request.post('/api/orders/complete', null, {
      params: { orderId }
    });
    return response.data;
  } catch (error) {
    console.error('完成订单失败:', error);
    throw error;
  }
};



