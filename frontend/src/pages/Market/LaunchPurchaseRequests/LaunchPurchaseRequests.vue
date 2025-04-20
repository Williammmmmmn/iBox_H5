<template>
  <div class="launch-page">
    <!-- 加载中状态 -->
    <van-loading v-if="loading" type="spinner" size="24px" color="#1989fa">
      加载中...
    </van-loading>

    <!-- 页面内容 -->
    <div v-else>
      <!-- 顶部导航栏 -->
      <van-nav-bar
        title="发起求购"
        left-arrow
        @click-left="$router.back()"
        :border="false"
        class="full-width-nav-bar"
      />

      <!-- 藏品信息展示 -->
      <div class="nft-display">
        <img :src="imageUrl" @error="handleImageError" class="nft-image" />
        <div class="nft-meta">
          <div class="nft-name">{{ name }}</div>
          <div class="nft-highest-price">
            <div class="highest-price-container">
              <span class="highest-price-text">
                当前最高价
                <span class="highest-price-value">￥{{ highestPrice }}</span>
              </span>
              <van-icon name="refresh" @click="refreshHighestPrice" class="refresh-icon" />
            </div>
          </div>
        </div>
      </div>

      <!-- 求购价格 -->
      <div class="purchase-price">
        <div class="price-title">求购价格</div>
        <div class="price-content">
          <span class="price-symbol">￥</span>
          <van-field
            v-model="purchasePrice"
            placeholder="请输入求购价格"
            type="number"
            maxlength="6"
            class="price-input"
          />
        </div>
      </div>

      <!-- 求购数量 -->
      <div class="purchase-quantity">
        <div class="quantity-header">
          <span>求购数量</span>
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
        <div class="quantity-note">最大支持9笔求购</div>
      </div>

      <!-- 支付方式 -->
      <div class="payment-method">
        <div class="section-title">支付方式</div>
        <div class="wallet-info">
          <van-icon name="balance-o" color="#1989fa" size="20" />
          <span class="wallet-name">默认钱包</span>
          <span class="wallet-balance">(余额：￥{{ balance }})</span>
          <van-icon name="success" color="#07c160" size="20" class="success-icon" />
        </div>
      </div>

      <!-- 底部按钮 -->
      <div class="submit-btn">
        <div class="total-amount">￥{{ totalAmount }}</div>
        <van-button
          round
          type="primary"
          class="confirm-button"
          :disabled="!purchasePrice || quantity === 0"
          @click="handleLaunch"
        >
          确认支付
        </van-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref,computed,onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { showToast } from 'vant';
import { useStore } from 'vuex';
import {getUserWalletBalance} from '@/api/wallet'; //获取用户钱包余额
import {createPurchaseRequest} from '@/api/purchaseRequest'; //发起求购请求
import { useRouter } from 'vue-router';

// 获取路由参数
const route = useRoute();
const router = useRouter();
const highestPrice = ref(route.query.highestPrice || 0); // 当前最高求购价
const name = ref(route.query.name || ''); // 藏品名称
const imageUrl = ref(route.query.imageUrl || ''); // 图片 URL
const nftId = ref(route.query.nftId || ''); // NFT ID

//获取当前登录用户地址
const store = useStore();
const walletAddress = computed(() => store.getters.getUserInfo?.walletAddress || '');

const loading = ref(true); // 加载状态，初始为 true

const totalAmount = computed(() => {
  const price = parseFloat(purchasePrice.value) || 0;
  const qty = parseInt(quantity.value, 10) || 0;
  return price * qty;
});
// 数据
const purchasePrice = ref(''); // 求购价格
const quantity = ref(1); // 求购数量
const balance = ref(0); // 默认余额

// 刷新最高求购价
const refreshHighestPrice = () => {
  // 模拟刷新逻辑
  highestPrice.value = Math.floor(Math.random() * 1000) + 100;
  showToast('已刷新最高求购价');
};

// 减少求购数量
const decreaseQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--;
  }
};

// 增加求购数量
const increaseQuantity = () => {
  if (quantity.value < 9) {
    quantity.value++;
  }
};

// 发起求购逻辑
const handleLaunch = async () => {
  if (!purchasePrice.value) {
    showToast('请输入求购价格');
    return;
  }
  if (totalAmount.value > balance.value) {
    showToast('余额不足');
    return;
  }
  if (!nftId.value) {
    showToast('藏品信息获取失败');
    return;
  }
  try {
    const response = await createPurchaseRequest(
      walletAddress.value,
      Number(route.query.nftId),
      parseFloat(purchasePrice.value),
      quantity.value
    );
    if (response.code === 200) {
      showToast('求购成功');
      // 可以在这里添加跳转逻辑，比如返回上一页
      setTimeout(() => {
        router.back();
      }, 1500);
    } else {
      showToast(response.message || '求购失败');
    }
  } catch (error) {
    console.error('发起求购请求失败:', error);
    showToast(error.response?.data?.message || '求购请求发送失败');
  }
};

// 组件挂载时获取钱包余额
onMounted(async () => {
  try {
    if (walletAddress.value) {
      const response = await getUserWalletBalance(walletAddress.value);
      balance.value = response.data; // 假设返回的数据结构中有 balance 字段
    } else {
      showToast('未获取到用户钱包地址');
    }
  } catch (error) {
    console.error('获取钱包余额失败:', error);
    showToast('获取钱包余额失败');
  } finally {
    loading.value = false; // 加载完成后设置为 false
  }
});
</script>

<style scoped>
.launch-page {
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
}

.nft-name {
    font-size: 18px;
    font-weight: bold;
    color: #333;
}

.nft-highest-price {
    margin-top: 8px;
}

.highest-price-container {
    display: flex;
    align-items: center;
    background: #f7f8fa;
    padding: 8px;
    border-radius: 8px;
}

.refresh-icon {
    position: absolute;
    margin-left: 120px;
    color: #0479ee;
    font-size: 13px;
    /* 确保图标大小适中 */
}

.refresh-icon::before {
    content: "刷新";

}

/* 求购价格 */
.purchase-price {
    display: flex;
    flex-direction: column;
    margin-top: 16px;
    background: #fff;
    padding: 12px;
    border-radius: 8px;
}

.price-symbol {
    font-size: 24px;
    font-weight: bold;
    color: #333;
    margin-right: 8px;
}

.price-input {
    flex: 1;
}

.highest-price-text {
    font-size: 10px;
    /* 调整字体大小 */
    color: #666;
    /* 字体颜色 */
}

.highest-price-value {
    font-size: 14px;
    /* 字体更大 */
    font-weight: bold;
    /* 加粗 */
    color: #000;
    /* 黑色字体 */
}

/* 求购数量 */
.purchase-quantity {
    background: #fff;
    font-size: 16px;
    /* 字体大小 */
    padding: 12px;
    font-weight: bold;
    /* 加粗 */
    color: #333;
    /* 字体颜色 */
    border-radius: 8px;
    margin-top: 16px;
}

.quantity-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.section-title{
    font-size: 16px;
    font-weight: bold;
}
.price-title {
    font-size: 16px;
    /* 字体大小 */
    font-weight: bold;
    /* 加粗 */
    color: #333;
    /* 字体颜色 */
    margin-bottom: 8px;
    /* 与输入框的间距 */
}

.price-content {
    display: flex;
    align-items: center;
    /* 垂直居中对齐 */
}

.quantity-controls {
    display: flex;
    align-items: center;
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

.quantity-btn:hover {
  background: #f0f0f0;
  transform: scale(1.05);
}

.quantity-btn:active {
  transform: scale(0.95);
}

.minus {
  border: 1px solid #ff4d4f;
  color: #ff4d4f;
}

.plus {
  border: 1px solid #52c41a;
  color: #52c41a;
}

.quantity-value {
  min-width: 24px;
  text-align: center;
  font-weight: 500;
  font-size: 16px;
  color: #333;
}

/* 添加点击动画 */
@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

.quantity-btn.clicked {
  animation: pulse 0.3s ease;
}

/* 悬浮效果增强 */
.quantity-btn:hover svg {
  transform: scale(1.2);
}

.quantity-btn svg {
  transition: transform 0.2s ease;
}
@media (max-width: 480px) {
  .quantity-selector {
    padding: 2px 8px;
  }
  
  .quantity-btn {
    width: 24px;
    height: 24px;
  }
  
  .quantity-value {
    font-size: 14px;
  }
}
.quantity-note {
    margin-top: 8px;
    font-size: 12px;
    color: #999;
    text-align: left;
}



/* 支付方式 */
.payment-method {
    background: #fff;
    padding: 12px;
    border-radius: 8px;
    margin-block: 16px;
}

.wallet-info {
    display: flex;
    align-items: center;
    margin-top: 8px;
}

.wallet-name {
    margin-left: 8px;
    font-size: 14px;
    color: #333;
}

.wallet-balance {
    margin-left: 8px;
    font-size: 12px;
    color: #999;
}
.success-icon {
  margin-left: auto; /* 将图标推到最右侧 */
}
.submit-btn {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 90%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 16px;
  background-color: #fff;
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.total-amount {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.confirm-button {
  width: 60%; /* 按钮宽度缩小 */
  height: 44px;
  font-size: 16px;
  font-weight: bold;
}
</style>