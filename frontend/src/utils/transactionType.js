export const getTransactionTypeName = (type) => {
    const typeMap = {
        PURCHASE: '购买',             // 普通购买
        SALE: '出售',                // 普通出售
        PURCHASE_REQUEST: '求购扣款',  // 发起求购（扣款）
        PURCHASE_REFUND: '求购退款',  // 求购取消（退款）
        CANCEL: '取消'               // 其他取消情况
    };
    return typeMap[type] || type; // 如果找不到映射，返回原值
  };