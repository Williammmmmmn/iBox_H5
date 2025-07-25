<template>
  <div>
    <!-- 顶部导航 -->
    <van-nav-bar class="top-nav" :border="false" left-arrow @click-left="goBack">
      <template #title>
        <!-- 使用按钮实现顶部切换 -->
        <div class="top-buttons">
          <button class="top-button" :class="{ active: currentView === 'market' }" @click="currentView = 'market'">
            市场
          </button>
          <button class="top-button" :class="{ active: currentView === 'myFocus' }" @click="currentView = 'myFocus'">
            我的关注
          </button>
        </div>
      </template>
    </van-nav-bar>
    <!-- 内容区域 -->
    <div v-if="currentView === 'market'" class="market-content">
      <!-- 滑动选项卡 -->
      <van-tabs v-model:active="activeTab" swipeable  @change="handleTabChange">
        <van-tab v-for="tab in tabs" :key="tab.name" :title="tab.title" :name="tab.name"></van-tab>
      </van-tabs>

      <div class="container-below">
        <!-- 搜索框 -->
        <van-search v-model="keyword" placeholder="输入藏品关键词" shape="round" @input="handleSearch" />

        <!-- 功能模块 -->
        <div class="features">
          <div class="dropdown-container">
            <!-- 类别下拉框 -->
            <div class="dropdown-button-category" @click="showCategoryPicker = true">
              <span>{{ selectedCategory || '全部类别' }}</span>
              <van-icon :name="showCategoryPicker ? 'arrow-up' : 'arrow-down'" size="8" />
            </div>
            <van-popup v-model:show="showCategoryPicker" round position="bottom">
              <van-picker :columns="categoryOptions" @cancel="showCategoryPicker = false"
                @confirm="onCategoryConfirm" />
            </van-popup>

            <!-- 发行份数下拉框 -->
            <div class="release-content-container" @click="showReleasePicker = true">
              <span>{{ selectedStatus || '发行份数' }}</span>
              <van-icon :name="showReleasePicker ? 'arrow-up' : 'arrow-down'" size="8" />
            </div>
            <van-popup v-model:show="showReleasePicker" round position="bottom">
              <van-picker :columns="statusOptions" @cancel="showReleasePicker = false" @confirm="onStatusConfirm" />
            </van-popup>
             <!-- 修改排序部分 -->
              <div class="price-content-container">
                <SortIndicator 
                  label="地板价" 
                  sort-key="price"
                  :current-sort="currentSort"
                  @sort-change="handleSortChange"
                />
              </div>
              
              <div class="volume-content-container">
                <SortIndicator 
                  label="成交量" 
                  sort-key="volume"
                  :current-sort="currentSort"
                  @sort-change="handleSortChange"
                />
              </div>
          </div>
        </div>
        <!-- 示例藏品 -->
        <div v-if="loading" class="loading">
          数据加载中...
        </div>
        <div v-else-if="filteredCollections.length > 0">
          <div v-for="item in filteredCollections" :key="item.id" class="collection-item"
            @click="goToConsignmentPage(item.nftId)">
            <!-- 图片 -->
            <div class="collection-image">
              <img :src="require(`@/${item.imageUrl}`)" alt="藏品图片" loading="lazy" />
            </div>

            <!-- 名称和发行/流通 -->
            <div class="collection-info">
              <div class="collection-name">{{ item.name }}</div>
              <div class="collection-stats">发行{{ item.totalIssueCount }}/流通{{ item.totalCirculateCount }}</div>
            </div>

            <!-- 五角星（收藏按钮） -->
            <div class="collection-favorite" @click.stop="toggleFavorite(item)">
              <!-- 亮的状态（isFavorite 为 true） -->
              <van-icon v-if="item.isFavorite" name="star" size="20" color="#ffd21e" />
              <!-- 灭的状态（isFavorite 为 false） -->
              <van-icon v-else name="star-o" size="20" color="#ccc" />
            </div>

            <!-- 地板价 -->
            <div class="collection-price">{{  item.lowestPrice > 0 ? item.lowestPrice : '--' }}</div>

            <!-- 成交量 -->
            <div class="collection-volume">{{ item.dailyTransactionCount > 0 ? item.dailyTransactionCount : '--' }}</div>
          </div>
        </div>
        <!-- 底部提示 -->
        <div class="bottom-tip" v-if="isReachBottom && !loading && filteredCollections.length > 0">
          没有更多了
        </div>
        <div v-else class="no-data">
          暂无更多藏品数据
        </div>
      </div>
      <!-- 我的关注内容（暂未实现） -->
      <div v-if="currentView === 'myFocus'" class="my-focus-content">
        <div>我的关注内容暂未实现</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, onActivated } from 'vue';
import { getNFTList } from '@/api/market';
import { useRouter } from 'vue-router';
import { useRoute } from 'vue-router';
import { showToast } from 'vant';
import SortIndicator from '@/components/SortIndicator.vue';
import { toFavorite, getFavoriteList } from '@/api/favorite';
import { useStore } from 'vuex';
const store = useStore();
const currentSort = ref({ key: '', order: '' });

const router = useRouter();
const route = useRoute();
const userId = store.getters.getUserInfo?.userId || '';
const activeTab = ref(route.query.tab || 'all');
const currentView = ref('market');
const tabs = [
  { name: '全部', title: '全部' },
  { name: '经典文化', title: '经典文化' },
  { name: '新春系列', title: '新春系列' },
  { name: '十年甄选', title: '十年甄选' },
  { name: '赛博系列', title: '赛博系列' },
  { name: '三国系列', title: '三国系列' },
];

const keyword = ref('');
const showCategoryPicker = ref(false);
const showReleasePicker = ref(false);
const selectedCategory = ref('');
const selectedStatus = ref('');
const isPriceAsc = ref(false);
const isPriceAsc2 = ref(false);
const isVolumeAsc = ref(false);
const isVolumeAsc2 = ref(false);
const originalCollections = ref([]);
const loading = ref(false); // 加载状态
const cachedData = {};//缓存已加载数据
const isReachBottom = ref(false);//是否到底部

const filteredCollections = computed(() => {
  let filtered = [...originalCollections.value];

  // 关键词搜索
  if (keyword.value) {
    const searchTerm = keyword.value.toLowerCase();
    filtered = filtered.filter(item =>
      item.name.toLowerCase().includes(searchTerm)
    );
  }

  // 类别筛选
  if (selectedCategory.value && selectedCategory.value !== '全部') {
    filtered = filtered.filter(item =>
      item.levelName === selectedCategory.value
    );
  }

  // 发行份数筛选
  if (selectedStatus.value && selectedStatus.value !== '全部') {
    const rangeMap = {
      '100以上': 100,
      '2000以上': 2000,
      '5000以上': 5000,
      '9000以上': 9000
    };
    const minCount = rangeMap[selectedStatus.value];
    filtered = filtered.filter(item => item.totalIssueCount > minCount);
  }

  return filtered;
});
const categoryOptions = [
  { text: '全部', value: '全部' },
  { text: '普通', value: '普通' },
  { text: '稀有', value: '稀有' },
  { text: '史诗', value: '史诗' },
];
const statusOptions = [
  { text: '全部', value: '全部' },
  { text: '100以上', value: '100以上' },
  { text: '2000以上', value: '2000以上' },
  { text: '5000以上', value: '5000以上' },
  { text: '9000以上', value: '9000以上' },
];
// 加载收藏状态
const loadFavorites = async () => {
  if (!userId) return;
  
  try {
    const response = await getFavoriteList(userId);
    
    const favoriteIds = response.data || [];
    
    const favoriteSet = new Set(favoriteIds.map(String)); // 统一转为字符串
    
    // 更新收藏状态
    originalCollections.value = originalCollections.value.map(item => ({
      ...item,
      isFavorite: favoriteSet.has(String(item.nftId)) 
    }));
    
  } catch (error) {
    console.error('加载收藏失败:', error);
    showToast('收藏状态加载失败');
  }
};
// 加载 NFT 数据
const loadNFTData = async (tag) => {
  const cacheKey = tag || 'all';
  loading.value = true;

  try {
    const data = await getNFTList(tag);

    // 初始化数据（保留可能的已有收藏状态）
    originalCollections.value = data.map(item => {
      // const cachedItem = cachedData[cacheKey]?.find(c => c.nftId === item.nftId);
      return {
        ...item,
        isFavorite:  false, 
        lowestPrice: item.lowestPrice || 0,
        dailyTransactionCount: item.dailyTransactionCount || 0
      };
    });
    await loadFavorites();
    cachedData[cacheKey] = originalCollections.value;

  } catch (error) {
    console.error('数据加载异常:', {
      tag,
      error: error.message
    });
    showToast('加载失败，请重试');
  } finally {
    loading.value = false;
  }
};

// 监听选项卡变化
const handleTabChange = (name) => {
  activeTab.value = name;
  const selectedTab = tabs.find(tab => tab.name === name);
  if (selectedTab) {
    const tag = selectedTab.name === 'all' ? null : selectedTab.name;
    loadNFTData(tag); // 根据选择的 tab 加载数据
  }
};

const onCategoryConfirm = (event) => {
  isVolumeAsc.value = false;
  isVolumeAsc2.value = false;
  isPriceAsc.value = false;
  isPriceAsc2.value = false;
  const selectedValue = event.selectedValues[0];
  selectedCategory.value = selectedValue; // 更新选择的类别
  showCategoryPicker.value = false;

};
const onStatusConfirm = (event) => {
  isVolumeAsc.value = false;
  isVolumeAsc2.value = false;
  isPriceAsc.value = false;
  isPriceAsc2.value = false;
  const selectedValue = event.selectedValues[0]; // 提取选中的值
  selectedStatus.value = selectedValue; // 更新选择的发行份数
  showReleasePicker.value = false;
};

const handleSortChange = (sort) => {
  currentSort.value = sort;
  
  if (sort.key === 'price') {
    filteredCollections.value.sort((a, b) => {
      const priceA = parseFloat(a.lowestPrice);
      const priceB = parseFloat(b.lowestPrice);
      return sort.order === 'asc' ? priceA - priceB : priceB - priceA;
    });
  } else if (sort.key === 'volume') {
    filteredCollections.value.sort((a, b) => {
      const volA = parseFloat(a.dailyTransactionCount);
      const volB = parseFloat(b.dailyTransactionCount);
      return sort.order === 'asc' ? volA - volB : volB - volA;
    });
  }
};

// 切换收藏状态
const toggleFavorite = async (item) => {
  if (!userId) {
    showToast('请先登录');
    router.push('/login');
    return;
  }

  try {
    await toFavorite({ 
      userId: userId, 
      nftId: item.nftId,
      actionType: item.isFavorite ? 0 : 1 // 1:收藏, 0:取消
    });
    
    // 更新本地状态
    item.isFavorite = !item.isFavorite;
    showToast(item.isFavorite ? '收藏成功' : '已取消收藏');
  } catch (error) {
    showToast('操作失败: ' + (error.message || '请重试'));
    console.error('收藏操作失败:', error);
  }
};
// 在setup中添加
const checkIfReachBottom = () => {
  const scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
  const windowHeight = window.innerHeight;
  const scrollHeight = document.documentElement.scrollHeight;

  // 距离底部50px时触发
  if (scrollTop + windowHeight >= scrollHeight - 50) {
    isReachBottom.value = true;
  } else {
    isReachBottom.value = false;
  }
};

onMounted(() => {
  window.addEventListener('scroll', checkIfReachBottom);
  loadNFTData(activeTab.value); // 初始加载数据
});

onUnmounted(() => {
  window.removeEventListener('scroll', checkIfReachBottom);
});
// 返回首页
const goBack = () => {
  router.push('/home');
};
//点击藏品调到对应寄售页
const goToConsignmentPage = (nftId) => {
  router.push({
    path: `/consignment`, // 使用路径格式
    query: {
      id: nftId,
      tab: activeTab.value
    }, // 使用 query 传递参数
  });
};
// 添加onActivated钩子，当页面从缓存中恢复时检查路由参数
onActivated(() => {
  if (route.query.tab) {
    activeTab.value = route.query.tab;
    handleTabChange(activeTab.value);
  }
});
</script>
<style>
.loading {
  text-align: center;
  padding: 20px;
  color: #999;
}

/* 顶部导航样式 */
.top-nav {
  background-color: #fff;
  position: relative;
}

.top-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  /* 按钮之间的间距 */
}

.top-button {
  padding: 8px 16px;
  font-size: 16px;
  color: #333;
  background: none;
  border: none;
  cursor: pointer;
}

.top-button.active {
  color: #41288c;
  font-weight: bold;
}

/* 内容区域样式 */
.market-content {
  overflow: visible;
  user-select: none;
  /* 确保父元素不会干扰子元素 */
}

.container-below {
  box-shadow: 0 -4px 10px rgba(0, 0, 0, 0.1);
  /* 添加顶部阴影 */
  margin-top: 6px;
  /* 添加一些间距，使阴影效果更明显 */
}

.my-focus-content {
  padding: 16px;
}

/* 选项卡内容样式 */
.tab-content {
  padding: 1px;
  text-align: center;
}

.van-tab {
  margin: 0 4px;
  /* 减少左右间距 */
  padding: 0;
  /* 去掉内边距 */
  flex: 1;
  /* 让选项卡均匀分布 */
  min-width: 76px;
  /* 设置最小宽度 */
}

/* 选项卡标题背景样式 */
.van-tab__text {
  display: flex;
  /* 使用flex布局 */
  align-items: center;
  /* 垂直居中 */
  justify-content: center;
  /* 水平居中 */
  background: linear-gradient(to bottom, #65aceb, #ffffff);
  /* 蓝白渐变背景 */
  border: none;
  border-radius: 5px;
  /* 圆角边框 */
  width: 100%;
  /* 根据实际需要调整宽度 */
  height: 100%;
  /* 高度与父元素一致 */
  line-height: 38px;
  /* 与高度一致，确保文本垂直居中 */
  text-align: center;
  /* 文本水平居中 */
  color: #333;
  /* 设置文字颜色 */
  white-space: nowrap;
  /* 防止文字换行 */
  padding: 0 8px;
  /* 减少内边距 */
}

.features {
  width: 100%;
  border-radius: 12px;
  background-color: #fff;
}

.custom-cell {
  margin-top: 6px;
  font-size: 18px;
  color: #333;
}

.dropdown-container {
  display: flex;
  gap: 10px;
  white-space: nowrap;
}

.dropdown-button-category {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 2px 13px;
  background-color: #eeecec;
  margin-left: 10px;
  border-radius: 12px;
  cursor: pointer;
  user-select: none;
  width: 66px;
  height: 24px;
  box-sizing: border-box;
}

.dropdown-button-category span {
  font-size: 10px;
  color: #444343;
}

.release-content-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 2px 13px;
  background-color: #eeecec;
  border-radius: 12px;
  cursor: pointer;
  user-select: none;
  width: 66px;
  height: 24px;
  box-sizing: border-box;
}

.release-content-container span {
  font-size: 10px;
  color: #444343;
}

.price-content-container {
  margin-left: 60px;
  display: flex;
  align-items: center;
}

.volume-content-container {
  margin-left: 5px;
  display: flex;
  align-items: center;
}

/* 示例藏品样式 */
.collection-item {
  display: flex;
  align-items: center;
  padding: 8px;
}

/* 图片 */
.collection-image {
  width: 60px;
  height: 60px;
  margin-right: 12px;
}

.collection-image img {
  width: 92%;
  height: 92%;
  border-radius: 8px;
  object-fit: cover;
}

/* 名称和发行/流通 */
.collection-info {
  flex: 1;
  margin-right: 10px;
  /* 减小右边距，使文字更靠近图片 */
  white-space: nowrap;
  /* 防止文字换行 */
  overflow: hidden;
  /* 超出部分隐藏 */
  text-overflow: ellipsis;
  /* 超出部分显示省略号 */
}

.collection-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
  white-space: nowrap;
  /* 防止名称换行 */
  overflow: hidden;
  /* 超出部分隐藏 */
  text-overflow: ellipsis;
  /* 超出部分显示省略号 */
}

.collection-stats {
  font-size: 10px;
  color: #868686;
  white-space: nowrap;
  /* 防止发行/流通信息换行 */
  overflow: hidden;
  /* 超出部分隐藏 */
  text-overflow: ellipsis;
  /* 超出部分显示省略号 */
}

/* 五角星（收藏按钮） */
.collection-favorite {
  position: absolute;
  /* 绝对定位 */
  left: 61%;
  /* 距离屏幕左侧 55% 的位置 */
  cursor: pointer;
  user-select: none;
}

/* 价格 */
.collection-price {
  position: absolute;
  /* 绝对定位 */
  left: 71%;
  /* 距离屏幕左侧 55% 的位置 */

  font-size: 12px;
  font-weight: bold;
  color: #000000;
}

/* 成交量 */
.collection-volume {
  position: absolute;
  /* 绝对定位 */
  left: 89%;
  /* 距离屏幕左侧 55% 的位置 */
  font-size: 12px;
  font-weight: bold;
  color: #000000;
}

.bottom-tip {
  padding: 15px 0;
  text-align: center;
  color: #999;
  font-size: 14px;
  background: #f7f7f7;
}

/* 暂无数据提示 */
.no-data {
  text-align: center;
  padding: 20px;
  color: #999;
}
</style>