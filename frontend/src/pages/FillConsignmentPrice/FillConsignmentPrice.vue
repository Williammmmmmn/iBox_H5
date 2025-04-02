<template>
    <div class="consignment-page">
      <van-nav-bar
        title="寄售"
        left-arrow
        @click-left="$router.back()"
        :border="false"
      />
  
      <!-- 藏品信息展示 -->
      <div class="nft-display">
        <img 
          :src="require(`@/${nftInfo.imageUrl}`)" 
          @error="handleImageError"
          class="nft-image"
        />
        <div class="nft-meta">
          <div class="nft-name">{{ nftInfo.name }}</div>
          <div class="nft-series">#{{ nftInfo.instanceNumber }}/{{ nftInfo.totalSupply }}</div>
          <div class="nft-id">标识: #{{ nftInfo.instanceNumber }}</div>
        </div>
      </div>
  
      <!-- 价格设置 -->
      <div class="price-section">
        <div class="section-title">设置售价</div>
        <div class="price-input-container">
          <span class="currency-symbol">￥</span>
          <van-field
            v-model="price"
            type="number"
            placeholder="请输入价格"
            class="price-input"
          />
        </div>
      </div>
  
      <!-- 收款账户 -->
      <div class="payment-section">
        <div class="section-title">收款账户</div>
        <div class="wallet-info">
          <van-icon name="balance-o" color="#1989fa" size="20" />
          <span class="wallet-name">默认钱包</span>
          <van-icon name="success" color="#07c160" size="20" />
        </div>
      </div>
  
      <!-- 预计收入 -->
      <div class="income-section">
        <div class="section-title">预计收入</div>
        <div class="income-details">
          <div class="income-row">
            <span>总售价</span>
            <span>￥{{ price || '0.00' }}</span>
          </div>
          <div class="income-row fee">
            <span>综合服务费 (5%)</span>
            <span>-￥{{ serviceFee }}</span>
          </div>
          <div class="income-row total">
            <span>预计收入</span>
            <span>￥{{ expectedIncome }}</span>
          </div>
        </div>
      </div>
  
      <!-- 确认按钮 -->
      <div class="submit-btn">
        <van-button 
          round 
          block 
          type="primary" 
          @click="submitConsignment"
          :disabled="!isPriceValid"
        >
          确认寄售
        </van-button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { showToast } from 'vant';
  import { useStore }  from 'vuex';
  import { submitConsignmentPublish } from '@/api/consignment';
  
  const route = useRoute();
  const router = useRouter();
  const store = useStore();
  // 从路由参数获取藏品信息
  const nftInfo = ref({
    id: route.query.nftId || '',
    name: route.query.nftName || '未知藏品',
    instanceNumber: route.query.instanceNumber || '0',
    totalSupply: route.query.totalSupply || '0',
    imageUrl: route.query.imageUrl || '',
    instanceId:route.query.instanceId || '',
  });
  
  // 处理图片加载失败
  const handleImageError = (e) => {
    e.target.src = require('@/assets/images/sorry.jpg');
  };
  
  // 价格相关逻辑
  const price = ref('');
  const isPriceValid = computed(() => Number(price.value) > 0);
  const serviceFee = computed(() => (Number(price.value) * 0.05).toFixed(2));
  const expectedIncome = computed(() => (Number(price.value) * 0.95).toFixed(2));
  
  // 提交寄售
  const submitConsignment = async () => {
    if (!isPriceValid.value) {
      showToast('请输入有效的价格');
      return;
    }
    
    try {
         // 获取当前用户钱包地址
        const walletAddress = store.getters.getUserInfo?.walletAddress;
        if (!walletAddress) {
            throw new Error('未获取到用户钱包地址');
        }
      await submitConsignmentPublish({
      instanceId: Number(nftInfo.value.instanceId), // 从路由参数获取的NFT实例ID
      sellerAddress: walletAddress,
      price: Number(price.value),
      currency: 'CNY' // 根据实际情况调整
    });
      
      showToast('寄售成功');
      setTimeout(() => router.back(), 1500);
    } catch (error) {
      showToast('寄售失败: ' + error.message);
    }
  };
  </script>
  
  <style scoped>
  .consignment-page {
    padding: 0 16px;
    background-color: #f7f8fa;
    min-height: 100vh;
    padding-bottom: 80px;
  }
  
  /* 藏品信息展示 */
  .nft-display {
    display: flex;
    padding: 16px 0;
    margin-bottom: 20px;
    background: #fff;
    border-radius: 8px;
    padding: 12px;
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
    margin-bottom: 4px;
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
  
  /* 价格设置 */
  .price-section {
    background: #fff;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 12px;
  }
  
  .section-title {
    font-weight: bold;
    font-size: 15px;
    margin-bottom: 12px;
    color: #333;
  }
  
  .price-input-container {
    display: flex;
    align-items: center;
  }
  
  .currency-symbol {
    font-weight: bold;
    font-size: 20px;
    margin-right: 8px;
  }
  
  .price-input {
    flex: 1;
    padding: 0;
    --van-field-input-text-color: #333;
    --van-field-input-font-size: 20px;
  }
  
  /* 收款账户 */
  .payment-section {
    background: #fff;
    border-radius: 8px;
    padding: 16px;
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
  
  /* 预计收入 */
  .income-section {
    background: #fff;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 12px;
  }
  
  .income-details {
    font-size: 14px;
  }
  
  .income-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;
  }
  
  .income-row.fee {
    color: #ee0a24;
  }
  
  .income-row.total {
    font-weight: bold;
    color: #333;
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px dashed #eee;
  }
  
  /* 确认按钮 */
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