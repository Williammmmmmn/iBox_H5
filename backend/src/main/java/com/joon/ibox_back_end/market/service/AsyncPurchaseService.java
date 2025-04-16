package com.joon.ibox_back_end.market.service;

import java.math.BigDecimal;

public interface AsyncPurchaseService {

     void logPurchaseSuccess(String buyerWalletAddress, BigDecimal sellerAmount);
}
