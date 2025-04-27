<template>
    <div class="order-detail-page">
      <!-- 顶部导航栏 -->
      <van-nav-bar
        :title="showStatusTitle ? orderDetail.status : '订单详情'"
        left-arrow
        @click-left="handleBack "
        :border="false"
        :title-style="titleStyle"
      />
  
      <!-- 主要内容区域 -->
      <div class="content-container">
        <!-- 藏品信息卡片 -->
        <div class="collection-card">
          <div class="collection-info">
            <img 
              :src="orderDetail.imageUrl" 
              alt="藏品图片" 
              class="collection-image"
            />
            <div class="collection-details">
              <div class="collection-name">{{ orderDetail.name }}</div>
              <div v-if="isPending" class="pay-button">
                <van-button 
                  type="primary" 
                  size="small"
                  @click="handlePay"
                >去支付</van-button>
              </div>
            </div>
          </div>
        </div>
  
        <!-- 订单信息卡片 -->
        <div class="order-info-card">
          <div class="card-title">订单信息</div>
          <div class="info-item">
            <span class="info-label">订单编号：</span>
            <span class="info-value">{{ orderDetail.orderId }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">订单类型：</span>
            <span class="info-value">{{ orderDetail.type }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">实付金额：</span>
            <span class="info-value price">￥{{ orderDetail.price }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">支付方式：</span>
            <span class="info-value">默认钱包</span>
          </div>
          <div class="info-item">
            <span class="info-label">创建时间：</span>
            <span class="info-value">{{ formatDate(orderDetail.createdAt) }}</span>
          </div>
          <div v-if="isPending" class="info-item">
            <span class="info-label">剩余支付时间：</span>
            <span class="info-value countdown">{{ countdownText }}</span>
          </div>
        </div>
      </div>
  
      <!-- 底部操作按钮 -->
      <div v-if="showActionButtons" class="action-buttons">
        <van-button 
          v-if="isRequesting || isPending"
          plain 
          type="danger" 
          block
          @click="handleCancel"
        >
          {{ isRequesting ? '取消求购' : '取消订单' }}
        </van-button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted,onUnmounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { useStore } from 'vuex';
  import { showConfirmDialog, showToast } from 'vant';
  import { getpayOrdersInfo } from '@/api/orders'; // 获取订单信息
  import {cancelPurchaseRequest } from '@/api/purchaseRequest'; // 取消订单和求购请求
  import { cancelOrder } from '@/api/buyLock'; // 取消订单
  const store = useStore();
  //钱包地址
  const walletAddress = computed(() => store.getters.getUserInfo?.walletAddress || '');
  const route = useRoute();
  const router = useRouter();
  const activeTab = ref(0);
  if (route.query && route.query.activeTab !== undefined) {
  activeTab.value = parseInt(route.query.activeTab) || 0;
}
// 从路由参数和查询参数中获取数据
const orderDetail = ref({
  orderId: route.query.orderId || '',
  name: route.query.name || '',
  imageUrl:require('@/'+route.query.imageUrl)  || require('@/assets/images/sorry.jpg'),
  price: route.query.price || 0,
  status: route.query.status || '',
  type: route.query.type || '',
  createdAt: route.query.createdAt || '',
});
  
  // 倒计时相关
  const countdown = ref(0); // 3分钟 = 180秒
  const countdownText = ref('00:00');
  let countdownTimer = null;

  // 从API获取的数据结构
const apiData = ref({
  nftId: '',
  instanceNumber: '',
  remainingSeconds: 0, // 后端计算的剩余秒数
  lockExpiry: null
});
  // 获取订单信息
const fetchOrderInfo = async () => {
  try {
    const response = await getpayOrdersInfo(orderDetail.value.orderId);
    apiData.value = response.data;
    
    // 初始化倒计时
    if (isPending.value) {
      countdown.value = apiData.value.remainingSeconds;
      startCountdown();
    }
  } catch (error) {
    console.error('获取订单信息失败:', error);
  }
};
  // 格式化日期
  const formatDate = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };
  
  // 启动倒计时
const startCountdown = () => {
  clearInterval(countdownTimer);
  updateCountdownText();
  
  countdownTimer = setInterval(() => {
    if (countdown.value <= 0) {
      clearInterval(countdownTimer);
      orderDetail.value.status = '已过期'; // 更新状态
      return;
    }
    countdown.value--;
    updateCountdownText();
  }, 1000);
};

// 更新倒计时显示
const updateCountdownText = () => {
  const minutes = Math.floor(countdown.value / 60).toString().padStart(2, '0');
  const seconds = (countdown.value % 60).toString().padStart(2, '0');
  countdownText.value = `${minutes}:${seconds}`;
};
  
// 处理支付操作
const handlePay = async () => {
  try {
    // 解析instanceId
    const instanceId = orderDetail.value.orderId.split('-')[1];
    
    // 跳转到购买页面
    router.push({
      path: '/buyPage',
      query: {
        nftId: apiData.value.nftId,
        instanceId: instanceId,
        instanceNumber: apiData.value.instanceNumber,
        price: orderDetail.value.price,
        name: orderDetail.value.name,
        imageUrl: orderDetail.value.imageUrl,
        // 可以添加其他必要参数
      }
    });
    
  } catch (error) {
    showToast('跳转支付页面失败');
    console.error('支付跳转错误:', error);
  }
};
  
  // 处理取消操作
  const handleCancel = async () => {
    const purchaseId = orderDetail.value.orderId.split('-')[1]; // 获取求购ID
    const instanceId = orderDetail.value.orderId.split('-')[1]; // 获取实例ID
    try {
      await showConfirmDialog({
        title: '确认取消',
        message: isRequesting.value 
          ? '确定要取消求购吗？' 
          : '确定要取消订单吗？'
      });
      
      // 根据当前是求购还是订单，执行相应的请求
    if (isRequesting.value) {
      // 如果是求购，调用求购取消的接口
      await cancelPurchaseRequest(walletAddress.value,Number(purchaseId));
      showToast('求购已取消');
    } else {
      // 如果是订单，调用订单取消的接口
       await cancelOrder(Number(instanceId));
      showToast('订单已取消');
    }
      router.back();
    } catch (error) {
      if (error !== 'cancel') {
        showToast('取消失败');
        console.error(error);
      }
    }
  };
  
  // 计算属性
  const isRequesting = computed(() => orderDetail.value.status === '求购中');
  const isPending = computed(() => orderDetail.value.status === '待支付');
  const showStatusTitle = computed(() => isRequesting.value || isPending.value);
  const showActionButtons = computed(() => isRequesting.value || isPending.value);
  
  const titleStyle = computed(() => {
    if (isRequesting.value) {
      return { color: '#1989fa' }; // 浅蓝色
    }
    return {};
  });
  const handleBack = () => {
  router.push({
    name: 'MyOrder',
    query: {
      activeTab: activeTab.value
    }
  });
};
  // 生命周期钩子
  onMounted(() => {
    fetchOrderInfo();
    // 如果是待支付订单，启动倒计时
    if (isPending.value) {
      startCountdown();
    }
    
    // 从路由参数获取数据
    if (route.params) {
      orderDetail.value = {
        ...orderDetail.value,
        ...route.params
      };
    }
  });
  
  // 清理定时器
  onUnmounted(() => {
    clearInterval(countdownTimer);
  });
  </script>
  
  <style scoped>
  .order-detail-page {
    background-color: #f7f8fa;
    min-height: 100vh;
    padding-bottom: 80px;
    user-select: none;
  }
  
  .content-container {
    padding: 16px;
  }
  
  /* 藏品信息卡片 - 修改布局 */
  .collection-card {
    background-color: #fff;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 12px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    position: relative; /* 添加相对定位 */
    min-height: 80px; /* 确保卡片有足够高度 */
  }
  
  .collection-info {
    display: flex;
    align-items: flex-start; /* 修改为顶部对齐 */
  }
  
  .collection-image {
    width: 80px;
    height: 80px;
    border-radius: 8px;
    object-fit: cover;
    margin-right: 12px;
  }
  
  .collection-details {
    flex: 1;
    display: flex;
    flex-direction: column;
    height: 80px; /* 与图片同高 */
  }
  
  .collection-name {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    text-align: right; /* 名称右对齐 */
    margin-bottom: auto; /* 将名称推到顶部 */
  }
  
  .pay-button {
    text-align: right; /* 按钮右对齐 */
    margin-top: auto; /* 将按钮推到底部 */
  }
  
  /* 订单信息卡片 - 修改对齐方式 */
  .order-info-card {
    background-color: #fff;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 12px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  }
  
  .card-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin-bottom: 16px;
    position: relative;
    padding-left: 8px;
  }
  
  .card-title::before {
    content: '';
    position: absolute;
    left: 0;
    top: 2px;
    bottom: 2px;
    width: 3px;
    background-color: #1989fa;
    border-radius: 2px;
  }
  
  .info-item {
    display: flex;
    margin-bottom: 12px;
    font-size: 14px;
    align-items: center; /* 垂直居中 */
    min-height: 24px; /* 确保每行高度一致 */
    white-space: nowrap;
  }
  
  .info-label {
    color: #999;
    width: 80px;
    flex-shrink: 0; /* 防止标签被压缩 */
  }
  
  .info-value {
    flex: 1;
    color: #333;
    text-align: right; /* 值右对齐 */
    padding-left: 10px; /* 与标签保持一定间距 */
  }
  
  .price {
    font-weight: bold;
    color: #ee0a24;
  }
  
  .countdown {
    margin-top: 4px;
    color: #ee0a24;
    font-weight: bold;
    display: inline-block; /* 确保倒计时不换行 */
    min-width: 50px; /* 为倒计时保留足够空间 */
  }
  
  /* 底部操作按钮 */
  .action-buttons {
    position: fixed;
    bottom: 20px;
    left: 16px;
    right: 16px;
  }
  
  .action-buttons .van-button {
    border-radius: 20px;
  }
  
  /* 新增样式确保布局一致性 */
  .requesting-info-card {
    background-color: #fff;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 12px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  }
  
  /* 确保所有卡片样式一致 */
  .order-info-card,
  .requesting-info-card {
    /* 共用样式 */
  }
  </style>