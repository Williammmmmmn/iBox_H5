import request from '@/utils/request';

export function submitConsignmentPublish(data) {
    return request({
      url: '/consignments/publishSale',
      method: 'post',
      data: {
        instanceId: data.instanceId,
        sellerAddress: data.sellerAddress,
        price: data.price,
        currency: data.currency || 'CNY'
      }
    });
  }