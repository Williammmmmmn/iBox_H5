<template>
    <div class="wallet-container">
      <!-- 顶部导航 -->
      <van-nav-bar
        title="我的钱包"
        left-arrow
        @click-left="$router.back()"
        :border="false"
      />
  
      <!-- 钱包卡片 -->
      <div class="wallet-card">
        <div class="wallet-bg">
          <div class="wallet-content">
            <div class="balance-title">账户余额 (元)</div>
            <div class="balance-amount">{{ formattedBalance }}</div>
          </div>
        </div>
      </div>
  
      <!-- 操作按钮 -->
      <div class="action-buttons">
        <van-button round type="primary" @click="showRecharge = true">
          <van-icon name="cash-back-record" /> 充值
        </van-button>
        <van-button round type="info" @click="showWithdraw = true">
          <van-icon name="cash-on-delivery" /> 提现
        </van-button>
      </div>
  
      <!-- 交易记录标题 -->
      <div class="section-title">
        <van-icon name="balance-list-o" /> 交易记录
      </div>
  
      <!-- 交易记录列表 -->
      <van-list
        v-model:loading="loading"
        :finished="finished"
        finished-text="没有更多了"
        @load="onLoad"
      >
        <van-cell
          v-for="(item, index) in transactions"
          :key="index"
          :title="item.type"
          :value="`${item.amount > 0 ? '+' : ''}${item.amount.toFixed(2)}`"
          :label="item.time"
          :class="{ 'income': item.amount > 0, 'expense': item.amount < 0 }"
        />
      </van-list>
  
      <!-- 充值弹窗 -->
      <van-popup v-model:show="showRecharge" round position="bottom">
        <div class="popup-content">
          <h3>充值</h3>
          <van-field v-model="rechargeAmount" label="充值金额" placeholder="请输入充值金额" type="number" />
          <van-button block type="primary" @click="handleRecharge">确认充值</van-button>
        </div>
      </van-popup>
  
      <!-- 提现弹窗 -->
      <van-popup v-model:show="showWithdraw" round position="bottom">
        <div class="popup-content">
          <h3>提现</h3>
          <van-field v-model="withdrawAmount" label="提现金额" placeholder="请输入提现金额" type="number" />
          <van-button block type="primary" @click="handleWithdraw">确认提现</van-button>
        </div>
      </van-popup>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted } from 'vue';
  import { showToast } from 'vant';
  
  // 钱包数据
  const balance = ref(12568.42);
//   const walletAddress = ref('0x00000000000000000000000000000000260D4DE4');
  const transactions = ref([]);
  const loading = ref(false);
  const finished = ref(false);
  
  // 弹窗控制
  const showRecharge = ref(false);
  const showWithdraw = ref(false);
  const rechargeAmount = ref('');
  const withdrawAmount = ref('');
  
  // 格式化余额显示
  const formattedBalance = computed(() => {
    return balance.value.toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });
  });
  
  // 加载交易记录
  const onLoad = () => {
    setTimeout(() => {
      // 模拟数据加载
      for (let i = 0; i < 10; i++) {
        transactions.value.push({
          type: ['充值', '消费', '提现'][Math.floor(Math.random() * 3)],
          amount: Math.random() > 0.5 ? 
            Math.random() * 1000 : 
            -Math.random() * 500,
          time: new Date(Date.now() - Math.random() * 7 * 24 * 60 * 60 * 1000).toLocaleString()
        });
      }
      loading.value = false;
      if (transactions.value.length >= 30) {
        finished.value = true;
      }
    }, 1000);
  };
  
  // 充值处理
  const handleRecharge = () => {
    const amount = parseFloat(rechargeAmount.value);
    if (isNaN(amount) || amount <= 0) {
      showToast('请输入有效的充值金额');
      return;
    }
    balance.value += amount;
    transactions.value.unshift({
      type: '充值',
      amount: amount,
      time: new Date().toLocaleString()
    });
    showRecharge.value = false;
    rechargeAmount.value = '';
    showToast('充值成功');
  };
  
  // 提现处理
  const handleWithdraw = () => {
    const amount = parseFloat(withdrawAmount.value);
    if (isNaN(amount) || amount <= 0) {
      showToast('请输入有效的提现金额');
      return;
    }
    if (amount > balance.value) {
      showToast('余额不足');
      return;
    }
    balance.value -= amount;
    transactions.value.unshift({
      type: '提现',
      amount: -amount,
      time: new Date().toLocaleString()
    });
    showWithdraw.value = false;
    withdrawAmount.value = '';
    showToast('提现申请已提交');
  };
  
  // 初始化加载数据
  onMounted(() => {
    onLoad();
  });
  </script>
  
  <style scoped>
  .wallet-container {
    min-height: 100vh;
    background-color: #f5f5f5;
  }
  
  .wallet-card {
    padding: 16px;
    margin: 16px;
  }
  
  .wallet-bg {
    background: linear-gradient(135deg, #9c27b0, #673ab7);
    border-radius: 16px;
    padding: 24px;
    box-shadow: 0 8px 20px rgba(156, 39, 176, 0.3);
    color: white;
    position: relative;
    overflow: hidden;
  }
  
  .wallet-bg::before {
    content: '';
    position: absolute;
    top: -50%;
    right: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 70%);
    transform: rotate(30deg);
  }
  
  .balance-title {
    font-size: 16px;
    opacity: 0.8;
    margin-bottom: 8px;
  }
  
  .balance-amount {
    font-size: 36px;
    font-weight: bold;
    margin-bottom: 16px;
    letter-spacing: 1px;
  }
  
  .wallet-address {
    font-size: 12px;
    opacity: 0.7;
    word-break: break-all;
  }
  
  .action-buttons {
    display: flex;
    justify-content: space-around;
    padding: 0 16px;
    margin: 24px 0;
  }
  
  .action-buttons .van-button {
    width: 45%;
  }
  
  .section-title {
    padding: 16px;
    font-size: 16px;
    font-weight: bold;
    color: #333;
    display: flex;
    align-items: center;
  }
  
  .section-title .van-icon {
    margin-right: 8px;
  }
  
  .popup-content {
    padding: 24px;
  }
  
  .popup-content h3 {
    text-align: center;
    margin-bottom: 24px;
  }
  
  .income .van-cell__value {
    color: #07c160;
  }
  
  .expense .van-cell__value {
    color: #ee0a24;
  }
  
  .van-nav-bar {
    background: transparent;
  }
  
  .van-nav-bar :deep(.van-nav-bar__title) {
    color: rgb(36, 36, 36);
  }
  
  .van-nav-bar :deep(.van-icon) {
    color: rgb(91, 88, 88);
  }
  </style>