<template>
  <div class="sell-page">
    <!-- 顶部导航栏 -->
    <van-nav-bar title="以求购价格售出" left-arrow @click-left="$router.back()" :border="false" class="full-width-nav-bar" />

    <!-- 求购信息展示 -->
    <div class="nft-display">
      <img :src="imageUrl" @error="handleImageError" class="nft-image" />
      <div class="nft-meta">
        <div class="nft-name">{{ name }}</div>
        <div class="nft-quantity">拥有数量：{{ quantity }}</div>
        <div class="nft-price">
          <span class="price-label">求购价</span>
          <span class="price-value">￥{{ price }}</span>
        </div>
      </div>
    </div>

    <!-- 选择售出编号 -->
    <div class="sell-number">
      <div class="section-title">
        选择售出编号
        <span class="subtitle">(仅支持单笔)</span>
      </div>
      <van-field v-model="sellNumber" placeholder="请选择编号" readonly is-link @click="showPopup = true"
        class="sell-number-input" />
    </div>
    <!-- 弹框选择编号 -->
    <van-popup v-model:show="showPopup" position="bottom" class="popup">
      <div class="popup-header">
        <span class="confirm-btn" @click="confirmSelection">确认</span>
        <span class="cancel-btn" @click="showPopup = false">取消</span>
      </div>
      <div class="popup-body">
        <div
          v-for="(item, index) in ownedInstances"
          :key="index"
          class="instance-item"
          @click="selectInstance(item)"
        >
          <span class="instance-number">#{{ item }}</span>
        </div>
      </div>
    </van-popup>
    <!-- 收款方式 -->
    <div class="payment-method">
      <div class="section-title">收款方式</div>
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
          <span class="label">总售价</span>
          <span class="value">￥{{ price }}</span>
        </div>
        <div class="income-row">
          <span class="label">综合服务费(5%)</span>
          <span class="value fee-value">-￥{{ serviceFee }}</span>
        </div>
        <div class="income-row total">
          <span class="label">预计收入</span>
          <span class="value">￥{{ estimatedIncome }}</span>
        </div>
      </div>
    </div>

    <!-- 底部按钮 -->
    <div class="submit-btn">
      <van-button round block type="primary" @click="handleSell">
        确认成交
      </van-button>
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router';
import { ref, computed } from 'vue';
import { showToast } from 'vant';

// 路由和数据
const route = useRoute();
const router = useRouter();

// 从路由参数获取数据
const price = Number(route.query.price) || 0; // 获取价格
const imageUrl = route.query.imageUrl || ''; // 获取图片 URL
const name = route.query.name || '求购商品'; // 获取商品名称
// const nftId = route.query.nftId || ''; // 获取 NFT ID
const quantity = ref(''); // 获取拥有数量

// 售出编号
const sellNumber = ref('');
const showPopup = ref(false); // 控制弹框显示

// 假设拥有的藏品实例编号
const ownedInstances = ref([101, 102, 103, 104, 105]);

// 计算综合服务费和预计收入
const serviceFee = computed(() => (price * 0.05).toFixed(2)); // 服务费 5%
const estimatedIncome = computed(() => (price - serviceFee.value).toFixed(2)); // 预计收入

// 处理图片加载失败
const handleImageError = (e) => {
  e.target.src = require('@/assets/images/sorry.jpg'); // 替换为默认图片
};

// 确认出售逻辑
const handleSell = async () => {
  if (!sellNumber.value) {
    showToast('请输入售出编号');
    return;
  }
  try {
    showToast('出售成功');
    setTimeout(() => {
      router.push('/success'); // 跳转到成功页面
    }, 1000);
  } catch (error) {
    showToast('出售失败，请稍后重试');
  }
};
</script>

<style scoped>
.sell-page {
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

/* 求购信息展示 */
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

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}
.subtitle {
  font-size: 12px; /* 字体较小 */
  color: #999; /* 灰色字体 */
  margin-left: 8px; /* 与主标题保持一定间距 */
}
.nft-meta {
  flex: 1;
  margin-left: 12px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.popup {
  background-color: #fff;
  border-radius: 16px 16px 0 0;
  padding: 12px;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 12px;
}

.confirm-btn {
  color: #0f7cf0; /* 绿色确认按钮 */
  cursor: pointer;
}

.cancel-btn {
  color: #151414; /* 红色取消按钮 */
  cursor: pointer;
}

.popup-body {
  display: flex;
  flex-wrap: wrap; /* 实现一行两个 */
  gap: 8px; /* 每个编号之间的间距 */
}

.instance-item {
  flex: 0 0 calc(40%); /* 每行两个，减去间距 */
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 12px;
  background-color: #f7f8fa;
  border: 1px solid #e0e0e0;
  border-radius: 8px; /* 圆角边框 */
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  text-align: center;
  transition: all 0.3s;
}

.instance-item:hover {
  background-color: #e6f7ff; /* 鼠标悬停时背景色 */
  border-color: #91d5ff;
}

.instance-number {
  color: #343434; /* 编号颜色 */
}
.nft-name {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.nft-quantity {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}

.nft-price {
  margin-top: 8px;
}

.price-label {
  font-size: 14px;
  color: #999;
  margin-right: 4px;
}

.price-value {
  font-size: 20px;
  font-weight: bold;
  color: #000;
}

/* 售出编号 */
.sell-number {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 12px;
}

.sell-number-input {
  margin-top: 8px;
}

/* 收款方式 */
.payment-method {
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
.fee-value {
  color: #ff4d4f; /* 红色字体 */
  font-weight: bold; /* 可选：加粗字体 */
}
.income-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.income-row {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  font-weight: bold;
  color: #666;
}

.income-row.total {
  font-size: 13px;
  color: #333;
}

/* 底部按钮 */
.submit-btn {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 90%;
  padding: 10px 16px;
  background-color: #fff;
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
  z-index: 100;
}
</style>