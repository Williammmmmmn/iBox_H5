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

    <!-- 交易记录列表 -->
  <div class="section-title">
    <van-icon name="balance-list-o" /> 交易记录
  </div>
  
  <!-- 添加标签页切换 -->
  <van-tabs v-model:active="activeTab" swipeable>
    <van-tab title="购买/出售">
      <van-list
        v-model:loading="loading.normal"
        :finished="finished.normal"
        finished-text="没有更多了"
        @load="loadNormalTransactions"
      >
        <van-cell
          v-for="(item, index) in normalTransactions"
          :key="'normal-'+index"
          :title="getTransactionType(item.type)"
          :value="getFormattedAmount(item.type, item.price)"
          :label="item.transactionDate.replace('T', ' ')"
          :class="getAmountClass(item.type, item.price)"
        />
      </van-list>
    </van-tab>
    
    <van-tab title="求购相关">
      <van-list
        v-model:loading="loading.purchase"
        :finished="finished.purchase"
        finished-text="没有更多了"
        @load="loadPurchaseTransactions"
      >
        <van-cell
          v-for="(item, index) in purchaseRequestTransactions"
          :key="'purchase-'+index"
          :title="getTransactionType(item.type)"
          :value="getFormattedAmount(item.type, item.price)"
          :label="item.transactionDate.replace('T', ' ')"
          :class="getAmountClass(item.type, item.price)"
        />
      </van-list>
    </van-tab>
  </van-tabs>

    <!-- 充值弹窗 -->
    <van-popup v-model:show="showRecharge" round position="bottom">
      <div class="popup-content">
        <h3>充值</h3>
        <van-field v-model="rechargeAmount" label="充值金额" placeholder="请输入充值金额(元)" type="number" />
        <van-button block type="primary" @click="handleRecharge">确认充值</van-button>
      </div>
    </van-popup>

    <!-- 提现弹窗 -->
    <van-popup v-model:show="showWithdraw" round position="bottom">
      <div class="popup-content">
        <h3>提现</h3>
        <van-field v-model="withdrawAmount" label="提现金额" placeholder="请输入提现金额(元)" type="number" />
        <van-button block type="primary" @click="handleWithdraw">确认提现</van-button>
      </div>
    </van-popup>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { showToast } from 'vant';
import { getWalletInfo, rechargeWallet, withdrawWallet, getWalletTransactions } from '@/api/wallet';
import { useStore } from 'vuex';
import { getTransactionTypeName } from '@/utils/transactionType';

const store = useStore();

// 从Vuex获取用户信息
const userProfile = computed(() => {
  const user = store.getters.getUserInfo || {};
  return {
    walletAddress: user.walletAddress || ''
  };
});

// 钱包数据
const balance = ref(0);
const walletAddress = ref(userProfile.value.walletAddress);
// const transactions = ref([]);
// 加载状态（分开管理）
const loading = ref({
  normal: false,
  purchase: false
});
const finished = ref({
  normal: false,
  purchase: false
});
// 当前激活的标签页（0: 购买/出售，1: 求购相关）
const activeTab = ref(0);
// 交易记录数据（分开存储）
const normalTransactions = ref([]);
const purchaseRequestTransactions = ref([]);

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
// 获取带符号的金额显示
const getFormattedAmount = (type, price) => {
  const amount = Number(price);
  if (isNaN(amount)) return '0.00';
  
  let sign = '';
  if (type === 'PURCHASE' || type === 'PURCHASE_REQUEST') {
    sign = '-';  // 购买或求购扣款，显示负号
  } else if (type === 'SALE' || type === 'PURCHASE_REFUND') {
    sign = '+';  // 出售或退款，显示正号
  }
  
  return `${sign}${Math.abs(amount).toFixed(2)}`;
};


// 获取金额样式类
const getAmountClass = (type, price) => {
  const amount = Number(price);
  if (isNaN(amount)) return '';
  
  if (type === 'PURCHASE' || type === 'PURCHASE_REQUEST') {
    return 'expense';  // 支出类
  } else if (type === 'SALE' || type === 'PURCHASE_REFUND' ) {
    return 'income';   // 收入类
  }
  return amount > 0 ? 'income' : 'expense';
};
// 加载钱包信息
const loadWalletInfo = async () => {
  try {
    const res = await getWalletInfo(walletAddress.value);
    balance.value = res.data.balance;
    walletAddress.value = res.data.walletAddress;
  } catch (error) {
    console.error('获取钱包信息失败:', error);
    showToast('获取钱包信息失败');
  }
};

// 加载交易记录
// 加载普通交易记录
const loadNormalTransactions = async () => {
  if (finished.value.normal || loading.value.normal) return;
  
  loading.value.normal = true;
  try {
    const res = await getWalletTransactions(walletAddress.value);
    const filtered = (res.data || []).filter(item => 
      item.type === 'PURCHASE' || item.type === 'SALE'
    );
    
    normalTransactions.value = [...normalTransactions.value, ...filtered];
    finished.value.normal = filtered.length ; 
  } catch (error) {
    console.error('加载失败:', error);
  } finally {
    loading.value.normal = false;
  }
};

// 加载求购相关记录
const loadPurchaseTransactions = async () => {
  if (finished.value.purchase || loading.value.purchase) return;
  
  loading.value.purchase = true;
  try {
    const res = await getWalletTransactions(walletAddress.value);
    
    // 1. 过滤出求购相关记录
    const filtered = (res.data || []).filter(item => 
      item.type === 'PURCHASE_REQUEST' || item.type === 'PURCHASE_REFUND'
    );
    
    // 2. 按 transactionDate 降序排序（最新的在前）
    const sorted = filtered.sort((a, b) => {
      return new Date(b.transactionDate) - new Date(a.transactionDate);
    });
    
    // 3. 更新数据
    purchaseRequestTransactions.value = sorted;
    
    // 4. 标记为已完成（假设API一次性返回所有数据）
    finished.value.purchase = true;
    
  } catch (error) {
    console.error('加载失败:', error);
  } finally {
    loading.value.purchase = false;
  }
};


const getTransactionType= (type) => {
  if (type === 'SALE') {
    return '出售'; // 如果类型为 SALE，返回 "出售"
  }
  return getTransactionTypeName(type);
};
// 充值处理
const handleRecharge = async () => {
  const amount = parseFloat(rechargeAmount.value);
  if (isNaN(amount) || amount <= 0) {
    showToast('请输入有效的充值金额');
    return;
  }
  
  try {
    await rechargeWallet(walletAddress.value, amount);
    showRecharge.value = false;
    rechargeAmount.value = '';
    await loadWalletInfo();
    showToast('充值成功');
  } catch (error) {
    console.error('充值失败:', error);
    showToast('充值失败');
  }
};

// 提现处理
const handleWithdraw = async () => {
  const amount = parseFloat(withdrawAmount.value);
  if (isNaN(amount) || amount <= 0) {
    showToast('请输入有效的提现金额');
    return;
  }
  
  try {
    await withdrawWallet(walletAddress.value, amount);
    showWithdraw.value = false;
    withdrawAmount.value = '';
    await loadWalletInfo();
    showToast('提现成功');
  } catch (error) {
    console.error('提现失败:', error);
    showToast(error.response?.data?.message || '提现失败');
  }
};

// 初始化加载数据
onMounted(() => {
  loadWalletInfo();
  loadNormalTransactions();
  loadPurchaseTransactions();
});
</script>

<style scoped>
.wallet-container {
  padding: 16px;
  user-select: none;
}

.wallet-card {
  margin: 16px 0;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.wallet-bg {
  background: linear-gradient(135deg, #1976d2, #2196f3);
  padding: 24px;
  color: white;
}

.balance-title {
  font-size: 14px;
  opacity: 0.9;
}

.balance-amount {
  font-size: 32px;
  font-weight: bold;
  margin: 8px 0;
}


.action-buttons {
  display: flex;
  justify-content: space-around;
  margin: 24px 0;
}

.action-buttons .van-button {
  width: 45%;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  margin: 16px 0;
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
  margin-bottom: 20px;
}

.income .van-cell__value {
  color: #4caf50;
}

.expense .van-cell__value {
  color: #f44336;
}
</style>