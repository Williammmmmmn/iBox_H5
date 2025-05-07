<template>
  <div class="my-order-page">
    <!-- 顶部导航栏 -->
    <van-nav-bar
      title="我的订单"
      left-arrow
      @click-left="navigateBack"
      :border="false"
      class="full-width-nav-bar"
    />

    <!-- 顶部选项卡 -->
    <van-tabs 
      :active ="activeTab" 
      @change="onTabChange" 
      scrollable
      class="scrollable-tabs"
    >
      <van-tab title="待支付" />
      <van-tab title="求购中" />
      <van-tab title="已完成" />
      <van-tab title="全部" />
    </van-tabs>

    <!-- 订单列表容器 -->
    <div class="order-list-container" ref="listContainer">
      <!-- 初始加载骨架屏 -->
      <div v-if="initialLoading" class="skeleton-loading">
        <div v-for="n in 5" :key="n" class="skeleton-item">
          <van-skeleton title :row="3" />
        </div>
      </div>
      
      <!-- 订单列表 -->
      <div v-else class="order-list">
        <div v-if="filteredOrders.length === 0" class="empty-state">
          <van-empty description="暂无订单" />
        </div>
        
        <template v-else>
          <transition-group name="list" tag="div">
            <div
              v-for="order in filteredOrders"
              :key="order.orderId"
              class="order-item"
              @click="goToOrderDetail(order)"
            >
              <div class="order-header">
                <span class="order-id">订单编号：{{ truncate(order.orderId, 10) }}</span>
                <span class="order-status" :class="getStatusClass(order.status)">{{ order.status }}</span>
              </div>
              <div class="order-body">
                <img 
                  :src="order.imageUrl ? require(`@/${order.imageUrl}`) : require('@/assets/images/sorry.jpg')" 
                  alt="藏品图片" 
                  class="order-image"
                  loading="lazy"
                />
                <div class="order-info">
                  <div class="order-name">{{ order.name }}
                    <span class="order-price">￥{{ order.price }}</span>
                  </div>
                  <div class="order-time">{{ formatDate(order.createdAt) }}</div>
                </div>
              </div>
            </div>
          </transition-group>
          
          <!-- 加载更多指示器 -->
          <div v-if="loadingMore" class="loading-more">
            <van-loading size="18px">加载更多...</van-loading>
          </div>
          
          <div v-if="finished && filteredOrders.length > 0" class="no-more">
            没有更多了
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick,onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';
import { useRouter,useRoute  } from 'vue-router';
import { getOrders } from '@/api/orders';
import { showToast } from 'vant';

const store = useStore();
const walletAddress = computed(() => store.getters.getUserInfo?.walletAddress || '');
const listContainer = ref(null);
const router = useRouter();
const route = useRoute(); 
// 订单数据
const orders = ref([]);
const initialLoading = ref(true);
const loadingMore = ref(false);
const finished = ref(false);
const activeTab = ref(0);
const pageNum = ref(1);
const pageSize = 10;

// 监听滚动到底部
const handleScroll = () => {
  const container = listContainer.value;
  if (!container || loadingMore.value || finished.value) return;

  const { scrollTop, clientHeight, scrollHeight } = container;

  // 当滚动到底部时触发加载更多
  if (scrollHeight - (scrollTop + clientHeight) < 100) {
    loadMore();
  }
};

// 加载更多数据
const loadMore = async () => {
  if (loadingMore.value || finished.value) return;
  
  try {
    loadingMore.value = true;
    const type = ['pending', 'requesting', 'completed', 'all'][activeTab.value];
    const response = await getOrders(walletAddress.value, type, pageNum.value, pageSize);
    
    if (response.data.list.length > 0) {
      // 对新数据进行时间排序（最新的在前）
      const sortedNewItems = response.data.list.sort((a, b) => {
        return new Date(b.createdAt) - new Date(a.createdAt);
      });
      
      // 合并并保持整体排序
      const mergedOrders = [...orders.value, ...sortedNewItems].sort((a, b) => {
        return new Date(b.createdAt) - new Date(a.createdAt);
      });
      
      // 使用 Set 去重（根据 orderId）
      orders.value = [...new Map(mergedOrders.map(item => [item.orderId, item])).values()];
      
      pageNum.value++;
      
      // 判断是否还有更多数据
      const totalItems = response.data.total || 0;
      const loadedItems = orders.value.length;
      finished.value = loadedItems >= totalItems;
    } else {
      finished.value = true;
    }
  } catch (error) {
    showToast('加载订单失败');
    console.error(error);
  } finally {
    initialLoading.value = false;
    loadingMore.value = false;
  }
};

// 监听 Tab 切换
const onTabChange = (index) => {
  
  activeTab.value = index;
  resetList();
};
// 重置列表
const resetList = () => {
  pageNum.value = 1;
  orders.value = [];
  finished.value = false;
  initialLoading.value = true;
  loadMore();
};
// 保留您原有的所有工具方法
const truncate = (text, length) => {
  if (!text) return '';
  if (text.length > length) {
    return text.slice(0, length) + '...';
  }
  return text;
};

const formatDate = (date) => {
  if (!date) return '';
  const options = { 
    year: 'numeric', 
    month: '2-digit', 
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  };
  return new Date(date).toLocaleString('zh-CN', options);
};

const getStatusClass = (status) => {
  switch (status) {
    case '待支付': return 'status-pending';
    case '求购中': return 'status-requesting';
    case '已完成': return 'status-completed';
    default: return '';
  }
};

// 初始化加载
onMounted(() => {
  if (route.query && route.query.activeTab !== undefined) {
    activeTab.value = parseInt(route.query.activeTab) || 0;
  }
  loadMore();
  nextTick(() => {
    const container = listContainer.value;
    if (container) {
      container.addEventListener('scroll', handleScroll);
    }
  });
});

onUnmounted(() => {
  const container = listContainer.value;
  if (container) {
    container.removeEventListener('scroll', handleScroll);
  }
});

// 筛选订单
const filteredOrders = computed(() => {
  return [...orders.value].sort((a, b) => {
    return new Date(b.createdAt) - new Date(a.createdAt);
  });
});
// 跳转到订单详情页
// 在订单列表页面的 goToOrderDetail 方法中
const goToOrderDetail = (order) => {
  router.push({
    name: 'OrderDetail', // 使用路由名称而不是路径
    
    query: {
      ...order,
      activeTab: activeTab.value 
    }
  });
};
const navigateBack = () => {
  router.push('/user');
};
</script>

<style scoped>
.my-order-page {
  background-color: #f7f8fa;
  min-height: 100vh;
  padding-bottom: 20px;
  user-select: none;
}

.full-width-nav-bar {
  background-color: #fff;
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 10;
}
.order-list-container {
  height: 100vh; /* 根据页面布局调整高度 */
  overflow-y: auto; /* 启用垂直滚动 */
}
.van-tabs {
  margin-top: 46px;
  background-color: #fff;
}

.scrollable-tabs {
  overflow-x: auto !important;
  -webkit-overflow-scrolling: touch;
}

:deep(.van-tabs__nav) {
  display: inline-block;
  white-space: nowrap;
}

:deep(.van-tab) {
  display: inline-block;
  flex: none;
  min-width: 80px;
  padding: 0 12px;
}

:deep(.van-tabs__wrap) {
  overflow-x: auto;
}


:deep(.van-tabs__line) {
  display: none !important;
}
.order-list {
  padding: 16px;
}

.order-item {
  background: #fff;
  border-radius: 8px;
  margin-bottom: 12px;
  padding: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.order-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
  color: #cd4f4f;
}

.order-id {
  font-size: 12px;
  color: #999;
}

.order-status {
  font-size: 12px;
  font-weight: bold;
}

.status-pending {
  color: #ee0a24;
}

.status-requesting {
  color: #1989fa;
}

.status-completed {
  color: #07c160;
}

.order-body {
  display: flex;
  align-items: center;
}

.order-image {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
  margin-right: 12px;
}

.order-info {
  flex: 1;
}

.order-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-price {
  font-size: 16px;
  font-weight: bold;
  color: #000;
  margin-left: 8px;
}

.order-time {
  font-size: 12px;
  color: #999;
  margin-top: 32px;
}

.no-more {
  text-align: center;
  font-size: 14px;
  color: #999;
  margin-top: 16px;
}

.empty-state {
  margin-top: 20px;
}

.loading-state {
  display: flex;
  justify-content: center;
  padding: 20px;
}

/* 列表动画 */
.list-enter-active,
.list-leave-active {
  transition: all 0.3s ease;
}
.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateY(30px);
}
.order-item {
  cursor: pointer;
  transition: transform 0.2s;
}

.order-item:active {
  transform: scale(0.98);
}
</style>