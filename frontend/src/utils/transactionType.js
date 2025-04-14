export const getTransactionTypeName = (type) => {
    const typeMap = {
      PURCHASE: '购买',
      PURCHASE_REQUEST: '求购',
      CANCEL:'取消'
    };
    return typeMap[type] || type; // 如果找不到映射，返回原值
  };