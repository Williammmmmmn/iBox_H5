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


