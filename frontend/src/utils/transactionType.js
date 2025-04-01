export const getTransactionTypeName = (type) => {
    const typeMap = {
      PURCHASE: '购买',
      SALE: '出售',
      TRANSFER: '转账',
      CONSIGNMENT: '寄售',
      PURCHASE_REQUEST: '求购'
    };
    return typeMap[type] || type; // 如果找不到映射，返回原值
  };