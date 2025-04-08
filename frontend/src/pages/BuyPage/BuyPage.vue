<template>
    <div class="buy-page">
      <!-- 顶部导航栏 -->
      <van-nav-bar
        title="确认订单"
        left-arrow
        @click-left="$router.back()"
        :border="false"
        class="full-width-nav-bar"
      />
  
      <!-- 藏品信息展示 -->
      <div class="nft-display">
        <img 
          :src="imageUrl" 
          @error="handleImageError"
          class="nft-image"
        />
        <div class="nft-meta">
          <div class="nft-name">{{ nftName }}</div>
          <div class="nft-series">#{{ instanceNumber }}</div>
          <div class="nft-id">标识: #{{ instanceNumber }}</div>
        </div>
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
            <span class="label">应付金额</span>
            <span class="price">￥{{ price }}</span>
          </div>
        </div>
      </div>
  
      <!-- 底部按钮 -->
      <div class="submit-btn">
        <van-button 
          round 
          block 
          type="primary" 
          @click="handlePurchase"
        >
          立即购买
        </van-button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { showToast } from 'vant';
  import { useStore } from 'vuex';
  import { purchaseItem } from '@/api/market';

  const route = useRoute();
  const router = useRouter();
  const store = useStore();
  
  // 从路由参数获取数据
  const nftId = route.query.nftId || '';
  const instanceId = Number(route.query.instanceId) || '';
  const instanceNumber = route.query.instanceNumber || '';
  const price = Number(route.query.price) || '0.00';
  const imageUrl = route.query.imageUrl || '';
  const nftName = route.query.name || 'NFT#'; // 示例名称
  // 控制按钮状态，防止重复提交
  const isProcessing = ref(false);
  // 从 Vuex 获取钱包地址
  const walletAddress = store.getters.getUserInfo?.walletAddress || '';
  
  // 处理图片加载失败
  const handleImageError = (e) => {
    e.target.src = require('@/assets/images/sorry.jpg');
  };
  
  // 处理购买逻辑
  const handlePurchase = async () => {
    if (!walletAddress) {
      showToast('未获取到钱包地址，请登录后重试');
      return;
    }
    try {
      isProcessing.value = true; // 设置为处理中
      const result = await purchaseItem(walletAddress, instanceId,price);
      console.log('购买成功:', result);
      showToast('购买成功');
      setTimeout(() => {
      router.push({
        path: '/consignment',
        query: {
          id: nftId, // 动态传递 nftId 参数
        },
      });
    }, 1000); // 跳转
    } catch (error) {
      showToast('购买失败: ' + error.message);
    }finally {
    isProcessing.value = false; // 请求完成后重置状态
  }
  };
  </script>
  
  <style scoped>
  .buy-page {
    background-color: #f7f8fa;
    min-height: 100vh;
    padding: 0 16px;
    padding-bottom: 80px;
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
    color: #666; /* 灰色 */
  }
  
  .income-row .price {
    font-size: 18px;
    font-weight: bold;
    color: #000; /* 黑色 */
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
  </style>