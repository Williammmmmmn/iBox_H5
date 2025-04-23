import request from '@/utils/request';


// 获取订单列表（带分页）
export const getOrders = (walletAddress, type = 'all', pageNum = 1, pageSize = 10) => {
  return request.get('/orders/myOrders', {
    params: { 
      walletAddress,
      type,
      pageNum,
      pageSize
    }
  });
};
//获取待支付订单的nftId和编号,剩余时间
export const getpayOrdersInfo = (orderId) => {
  return request.get('/orders/paymentInfo', {
    params: { 
      orderId
    }
  });
};