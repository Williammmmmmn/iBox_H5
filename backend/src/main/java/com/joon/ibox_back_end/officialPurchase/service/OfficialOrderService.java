package com.joon.ibox_back_end.officialPurchase.service;

import com.joon.ibox_back_end.officialPurchase.entity.OrderRequest;

public interface OfficialOrderService {


    String createOrder(OrderRequest request);

    boolean completeOrder(String orderId);
}
