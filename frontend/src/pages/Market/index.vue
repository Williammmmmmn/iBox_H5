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
      <van-tabs v-model:active="activeTab" swipeable class="custom-tabs">
        <van-tab v-for="tab in tabs" :key="tab.name" :title="tab.title" :name="tab.name"></van-tab>
      </van-tabs>

      <div class="container-below">
        <!-- 搜索框 -->
        <van-search v-model="keyword" placeholder="输入藏品关键词" shape="round" />

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

            <!-- 地板价排序 -->
            <div class="price-content-container" @click="toggleSort('price')">
              <div class="text-container">
                <span class="text">地板价</span>
                <div class="triangle-container">
                  <span class="triangle triangle-up" :class="{ active: isPriceAsc }"></span>
                  <span class="triangle triangle-down" :class="{ active: !isPriceAsc }"></span>
                </div>
              </div>
            </div>

            <!-- 成交量排序 -->
            <div class="volume-content-container" @click="toggleSort('volume')">
              <div class="text-container">
                <span class="text">成交量</span>
                <div class="triangle-container">
                  <span class="triangle triangle-up" :class="{ active: isVolumeAsc }"></span>
                  <span class="triangle triangle-down" :class="{ active: !isVolumeAsc }"></span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 示例藏品 -->
        <div v-if="filteredCollections.length > 0">
          <div v-for="item in filteredCollections" :key="item.name" class="collection-item">
            <!-- 图片 -->
            <div class="collection-image">
              <img :src="item.img" alt="藏品图片" />
            </div>

            <!-- 名称和发行/流通 -->
            <div class="collection-info">
              <div class="collection-name">{{ item.name }}</div>
              <div class="collection-stats">发行{{ item.issued }}/流通{{ item.circulation }}</div>
            </div>

            <!-- 五角星（收藏按钮） -->
            <div class="collection-favorite" @click="toggleFavorite(item)">
              <!-- 亮的状态（isFavorite 为 true） -->
              <van-icon v-if="item.isFavorite" name="star" size="20" color="#ffd21e" />
              <!-- 灭的状态（isFavorite 为 false） -->
              <van-icon v-else name="star-o" size="20" color="#ccc" />
            </div>

            <!-- 价格 -->
            <div class="collection-price">{{ item.price }}</div>

            <!-- 成交量 -->
            <div class="collection-volume">{{ item.volume }}</div>
          </div>
        </div>
        <div v-else class="no-data">
          暂无藏品数据
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
import { ref, computed, reactive } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const activeTab = ref('all'); // 默认选中第一个 tab
const currentView = ref('market');
const tabs = [
  { name: 'all', title: '全部' },
  { name: 'classic', title: '经典文化' },
  { name: 'newYear', title: '新春系列' },
  { name: 'tenYears', title: '十年甄选' },
  { name: 'cyber', title: '赛博系列' },
  { name: 'threeKingdoms', title: '三国系列' },
];

const keyword = ref('');
const showCategoryPicker = ref(false);
const showReleasePicker = ref(false);
const selectedCategory = ref('');
const selectedStatus = ref('');
const isPriceAsc = ref(false);
const isVolumeAsc = ref(false);

const categoryOptions = [
  { text: '全部类别', value: '全部类别' },
  { text: '类别1', value: '类别1' },
  { text: '类别2', value: '类别2' },
];
const statusOptions = [
  { text: '全部', value: '全部' },
  { text: '进行中', value: '进行中' },
  { text: '已结束', value: '已结束' },
];

const collections = reactive({
  all: [
    { name: '2025纵酒', issued: 1000, circulation: 713, price: 11888, volume: 2, img: require('@/assets/images/saveMom.jpg'), isFavorite: false },
    { name: '2025日进斗金', issued: 3000, circulation: 2215, price: 12999, volume: 9, img: require('@/assets/images/saveMom.jpg'), isFavorite: true },
    { name: '2025于禁', issued: 6666, circulation: 6219, price: 3000, volume: 68, img: require('@/assets/images/saveMom.jpg'), isFavorite: false },
    { name: '2025太史慈', issued: 2278, circulation: 2278, price: 18000, volume: 1, img: require('@/assets/images/saveMom.jpg'), isFavorite: false },
    { name: '至臻-潇湘咖啡', issued: 581, circulation: 258, price: 8444, volume: 2, img: require('@/assets/images/saveMom.jpg'), isFavorite: false },
    { name: '2025张郃', issued: 5000, circulation: 4994, price: 3499, volume: 42, img: require('@/assets/images/saveMom.jpg'), isFavorite: false },
    { name: '2025纵酒', issued: 1000, circulation: 713, price: 11888, volume: 2, img: require('@/assets/images/saveMom.jpg'), isFavorite: false },
    { name: '2025日进斗金', issued: 3000, circulation: 2215, price: 12999, volume: 9, img: require('@/assets/images/saveMom.jpg'), isFavorite: true },
    { name: '2025于禁', issued: 6666, circulation: 6219, price: 3000, volume: 68, img: require('@/assets/images/saveMom.jpg'), isFavorite: false },
    
  ],
  classic: [
    { name: '经典文化1', issued: 1000, circulation: 713, price: 11888, volume: 2, isFavorite: false, },
    { name: '经典文化2', issued: 3000, circulation: 2215, price: 12999, volume: 9, isFavorite: false, },
  ],
  newYear: [
    { name: '新春系列1', issued: 1000, circulation: 713, price: 11888, volume: 2, isFavorite: false, },
    { name: '新春系列2', issued: 3000, circulation: 2215, price: 12999, volume: 9, isFavorite: false, },
  ],
  tenYears: [
    { name: '十年甄选1', issued: 1000, circulation: 713, price: 11888, volume: 2, isFavorite: false, },
    { name: '十年甄选2', issued: 3000, circulation: 2215, price: 12999, volume: 9, isFavorite: false, },
  ],
  cyber: [
    { name: '赛博系列1', issued: 1000, circulation: 713, price: 11888, volume: 2, isFavorite: false, },
    { name: '赛博系列2', issued: 3000, circulation: 2215, price: 12999, volume: 9, isFavorite: false, },
  ],
  threeKingdoms: [
    { name: '三国系列1', issued: 1000, circulation: 713, price: 11888, volume: 2, isFavorite: false, },
    { name: '三国系列2', issued: 3000, circulation: 2215, price: 12999, volume: 9, isFavorite: false, },
  ],
}
)

const filteredCollections = computed(() => {
  let items = collections[activeTab.value] || []; // 确保 items 是一个数组

  // 根据关键词过滤
  if (keyword.value) {
    items = items.filter(item => item.name.includes(keyword.value));
  }

  return items;
});

const onCategoryConfirm = () => {
  showCategoryPicker.value = false;
};

const onStatusConfirm = () => {
  showReleasePicker.value = false;
};

const toggleSort = (type) => {
  if (type === 'price') {
    isPriceAsc.value = !isPriceAsc.value;
  } else if (type === 'volume') {
    isVolumeAsc.value = !isVolumeAsc.value;
  }
};
const toggleFavorite = (item) => {
  item.isFavorite = !item.isFavorite; // 切换收藏状态
  console.log('收藏状态已切换:', item.name, item.isFavorite);
};
// 返回首页
const goBack = () => {
  router.push('/home');
};
</script>

<style>
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

.text-container {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.text {
  font-size: 10px;
  color: #444343;
}

.triangle-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.triangle {
  display: inline-block;
  width: 0;
  height: 0;
  margin-left: 3px;
  border-left: 3px solid transparent;
  border-right: 3px solid transparent;
  margin: 1px;
}

.triangle-up {
  border-bottom: 4px solid #817f7f;
}

.triangle-down {
  border-top: 4px solid #817f7f;
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
  margin-right: 12px;
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
  margin-right: 10px;
  cursor: pointer;
  user-select: none;

}

/* 价格 */
.collection-price {
  font-size: 12px;
  font-weight: bold;
  color: #000000;
  margin-right: 32px;
}

/* 成交量 */
.collection-volume {
  margin-right: 18px;

  font-size: 12px;
  color: #000000;
}

/* 暂无数据提示 */
.no-data {
  text-align: center;
  padding: 20px;
  color: #999;
}
</style>