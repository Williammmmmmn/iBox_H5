import request from '@/utils/request';
//上架藏品
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