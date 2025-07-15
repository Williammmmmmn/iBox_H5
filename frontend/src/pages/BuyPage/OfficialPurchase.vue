<template>
    <div class="buy-page">
        <!-- 顶部导航栏 -->
        <van-nav-bar title="确认订单" left-arrow @click-left="$router.back()" :border="false" class="full-width-nav-bar" />

        <!-- 藏品信息展示 -->
        <div class="nft-display">
            <img :src="imageUrl" @error="handleImageError" class="nft-image" />
            <div class="nft-meta">
                <div class="nft-name">{{ nftName }}</div>
            </div>
        </div>

        <div class="purchase-quantity">
            <div class="quantity-header">
                <span>购买数量</span>
                <div class="quantity-controls">
                    <div class="quantity-selector">
                        <div class="quantity-btn minus" @click="decreaseQuantity">
                            <svg viewBox="0 0 24 24" width="12" height="12">
                                <path d="M5 11h14v2H5z" fill="currentColor" />
                            </svg>
                        </div>
                        <span class="quantity-value">{{ quantity }}</span>
                        <div class="quantity-btn plus" @click="increaseQuantity">
                            <svg viewBox="0 0 24 24" width="12" height="12">
                                <path d="M11 5h2v14h-2zm7 7v2H5v-2z" fill="currentColor" />
                            </svg>
                        </div>
                    </div>
                </div>
            </div>
            <div class="quantity-note">最大支持9笔购买</div>
        </div>

        <!-- 支付方式 -->
        <div class="payment-section">
            <div class="section-title">
                <van-icon name="credit-card" color="#1989fa" size="20" />
                <span class="title-text">支付方式</span>
            </div>
            <div class="wallet-info">
                <van-icon name="balance-o" color="#1989fa" size="20" />
                <span class="wallet-name">默认钱包</span>
                <van-icon name="success" color="#07c160" size="20" />
            </div>
        </div>

        <!-- 应付金额 -->
        <div class="income-section">
            <div class="income-details">
                <div class="income-row">
                    <span class="label">单价</span>
                    <span class="price">￥{{ price }}</span>
                </div>
                <div class="income-row">
                    <span class="label">数量</span>
                    <span class="price">x {{ quantity }}</span>
                </div>
                <div class="income-row total">
                    <span class="label">总计</span>
                    <span class="price">￥{{ totalAmount }}</span>
                </div>
            </div>
        </div>

        <!-- 底部按钮 -->
        <div class="submit-btn">
            <van-button round block type="primary" @click="handlePurchase">
                立即购买
            </van-button>
        </div>
    </div>
</template>

<script setup>
import { ref,computed} from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { showToast,showDialog } from 'vant';
import { useStore } from 'vuex';
import {createOrder,completeOrder } from '@/api/officialSale';

const route = useRoute();
const router = useRouter();
const store = useStore();
const orderId = ref(null);
// 从路由参数获取数据
const nftId = route.query.nftId || '';
const price = Number(route.query.price) || '0.00';
const imageUrl = route.query.imageUrl || '';
const nftName = route.query.name || '示例名称'; // 示例名称
// 控制按钮状态，防止重复提交
const isProcessing = ref(false);
// 从 Vuex 获取钱包地址
const walletAddress = store.getters.getUserInfo?.walletAddress || '';
const quantity = ref(1);
// 处理图片加载失败
const handleImageError = (e) => {
    e.target.src = require('@/assets/images/sorry.jpg');
};
// 计算总金额
const totalAmount = computed(() => {
  return (Number(price) * quantity.value).toFixed(2);
});
// 减少购买数量
const decreaseQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--;
  }
};

// 增加购买数量
const increaseQuantity = () => {
  if (quantity.value < 9) {
    quantity.value++;
  }
};
// 修改购买处理方法
const handlePurchase = async () => {
    if (!walletAddress) {
        showToast('未获取到钱包地址，请登录后重试');
        return;
    }

    try {
        // 先创建订单
        orderId.value = await createOrder(
            walletAddress, 
            nftId, 
            price, 
            quantity.value
        );

        // 使用 showDialog 替代 Dialog.confirm
        try {
            await showDialog({
                title: '确认购买',
                message: `确认要以 ￥${totalAmount.value} 购买该藏品吗？`,
                confirmButtonText: '确认购买',
                cancelButtonText: '再想想',
                showCancelButton: true,
            });
            
            // 用户点击确认
            isProcessing.value = true;
            await completeOrder(orderId.value);
            showToast('购买成功');
            
            // 跳转到详情页
            setTimeout(() => {
                router.push({
                    path: '/home',
                   
                });
            }, 1000);
            
        } catch (e) {
            // 用户点击取消
            orderId.value = null;
        } finally {
            isProcessing.value = false;
        }

    } catch (error) {
        showToast(error.message || '创建订单失败，请稍后重试');
    }
};
</script>

<style scoped>
.buy-page {
    background-color: #f7f8fa;
    min-height: 100vh;
    padding: 0 16px;
    padding-bottom: 80px;
    user-select: none;
}

.full-width-nav-bar {
    background-color: #f7f8fa;
    width: 100%;
    position: fixed;
    top: 0;
    left: 0;
    z-index: 10;
}

/* 藏品信息展示 */
.nft-display {
    padding: 12px;
    display: flex;
    margin-top: 46px;
    margin-bottom: 20px;
    background: #fff;
    border-radius: 8px;
}

.nft-image {
    width: 80px;
    height: 80px;
    border-radius: 8px;
    object-fit: cover;
    flex-shrink: 0;
}

.nft-meta {
    flex: 1;
    margin-left: 12px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    height: 80px;
}

.nft-name {
    font-size: 18px;
    font-weight: bold;
    color: #333;
    margin-bottom: 2px;
}

.nft-series {
    font-size: 15px;
    color: #666;
    margin-bottom: 4px;
}

.nft-id {
    display: inline-block;
    padding: 2px 8px;
    background-color: #f1df76;
    color: #83802b;
    font-size: 14px;
    border-radius: 4px;
    align-self: flex-start;
}

/* 支付方式 */
.payment-section {
    background: #fff;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 12px;
}

.section-title {
    display: flex;
    align-items: center;
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin-bottom: 12px;
}

.wallet-info {
    display: flex;
    align-items: center;
}

.wallet-name {
    margin-left: 8px;
    flex: 1;
    font-size: 14px;
}

/* 应付金额 */
.income-section {
    background: #fff;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 12px;
}

.income-details {
    font-size: 16px;
}

.income-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.income-row .label {
    font-size: 16px;
    font-weight: bold;
    color: #666;
    /* 灰色 */
}

.income-row .price {
    font-size: 18px;
    font-weight: bold;
    color: #000;
    /* 黑色 */
}

/* 底部按钮 */
.submit-btn {
    position: fixed;
    bottom: 20px;
    left: 16px;
    right: 16px;
}

.submit-btn .van-button {
    height: 44px;
    font-size: 16px;
}
.purchase-quantity {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 12px;
}

.quantity-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.quantity-selector {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: #f8f8f8;
  border-radius: 20px;
  padding: 4px 12px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.quantity-btn {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.2s ease;
  background: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  color: #555;
}

.quantity-value {
  min-width: 24px;
  text-align: center;
  font-weight: 500;
  font-size: 16px;
  color: #333;
}

.quantity-note {
  margin-top: 8px;
  font-size: 12px;
  color: #999;
}

/* 修改金额显示样式 */
.income-row {
  margin-bottom: 8px;
}

.income-row.total {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #eee;
}

.income-row.total .price {
  color: #f44;
  font-size: 20px;
}
</style>