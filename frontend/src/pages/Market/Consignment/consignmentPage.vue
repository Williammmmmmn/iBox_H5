<template>
  <div class="container">
    <!-- 背景图片容器 -->
    <div class="background-image" :style="{ backgroundImage: `url(${imageUrl})` }">
      <!-- 自定义的 left-arrow -->
      <div class="header">
        <div class="arrow-container" @click="goBack">
          <van-icon name="arrow-left" class="left-arrow" />
        </div>
      </div>
      <!-- 新图片容器 -->
      <div class="overlay-image-container">
        <img :src="imageUrl" class="overlay-image" />
      </div>
    </div>
    <!-- 图片名称 -->
    <div class="image-name">{{ name }}</div>
    <!-- 发行和流通信息 -->
    <div class="info-container">
      <div class="info-item">
        <span class="label">发行</span>
        <span class="value">{{ issueCount }}份</span>
      </div>
      <div class="info-item">
        <span class="label">流通</span>
        <span class="value">{{ circulationCount }}份</span>
      </div>
    </div>
    <!-- 内容区域 -->
    <div class="content">
      <div class="content">
        <van-tabs v-model:active="activeTab" @change="onTabChange">
          <!-- 市场价格 -->
          <van-tab title="市场价格">
            <div class="tab-content">
              <!-- Switch 和价格、编号 -->
              <div class="switch-and-info">
                <!-- 自定义 Switch -->
                <div class="custom-switch">
                  <div class="switch-background" :class="{ 'move-right': isSwitchOn === 'right' }"></div>
                  <div class="switch-item left" :class="{ active: isSwitchOn === 'left' }"
                    @click="toggleSwitch('left')">
                    寄售
                  </div>
                  <div class="switch-item right" :class="{ active: isSwitchOn === 'right' }"
                    @click="toggleSwitch('right')">
                    求购
                  </div>
                </div>
                <!-- 价格和编号 -->
                <div class="price-and-id" v-if="isSwitchOn === 'left'">
                  <!-- 价格排序部分 -->
                    <div class="price-content-container">
                      <SortIndicator 
                        label="价格" 
                        sort-key="price"
                        :current-sort="currentSort"
                        @sort-change="handleSortChange"
                      />
                    </div>
                  <!-- 编号排序部分 -->
                    <div class="volume-content-container">
                      <SortIndicator 
                        label="编号" 
                        sort-key="id"
                        :current-sort="currentSort"
                        @sort-change="handleSortChange"
                      />
                    </div>
                </div>
              </div>

              <!-- 数据展示区域 -->
              <div class="data-list">
                <!-- 加载中提示 -->
                <div v-if="loading" class="loading">
                  数据加载中...
                </div>
                
                <!-- 寄售模式 -->
                <template v-else-if="isSwitchOn === 'left'">
                  <van-empty v-if="currentDataList.length === 0" description="暂无数据" />
                  <template v-else>
                    <div v-for="(item, index) in currentDataList" :key="index" @click="goToSaleDetail(item.id)"
                      class="sale-item">
                      <!-- 左边：名称和编号 -->
                      <div class="left-info">
                        <div class="name">{{ item.name }}
                          <span v-if="item.lockExpiry && new Date(item.lockExpiry) > new Date()" class="locked-tag">
                            已锁定
                          </span>
                        </div>
                        <div class="id">
                          <span class="hash">#</span>
                          <span class="number">{{ item.id }}</span>
                        </div>
                      </div>
                      <!-- 右边：价格 -->
                      <div class="right-info">
                        <span class="price-symbol">￥</span>
                        <span class="price">{{ item.price }}</span>
                      </div>
                    </div>
                  </template>
                </template>
                
                <!-- 求购模式 -->
                <template v-else>
                  <van-empty v-if="purchaseList.length === 0" description="暂无求购记录" class="empty-purchase" />
                  <template v-else>
                    <div v-for="(item, index) in purchaseList" 
                        :key="'purchase-' + index" 
                        class="purchase-item"
                        @click="goToSellPurchaseRequests(item)">
                      <div class="purchase-price">
                        <span class="price-symbol">￥</span>
                        <span class="price">{{ item.price }}</span>
                      </div>
                      <van-icon name="arrow" class="chevron-icon" />
                    </div>
                  </template>
                  
                  <!-- 始终显示的发起求购按钮 -->
                  <div class="purchase-button-container">
                    <van-button
                      type="primary"
                      block
                      class="purchase-button"
                      @click="handleInitiatePurchase"
                    >
                      发起求购
                    </van-button>
                  </div>
                </template>
                
                <!-- 底部提示 -->
                <div v-if="isReachBottom" class="no-more">
                  --没有更多--
                </div>
              </div>
            </div>
          </van-tab>
          <!-- 相关公告 -->
          <van-tab title="相关公告">
            <div class="tab-content">
              <!-- 加载中提示 -->
              <van-loading v-if="loading" size="24px" vertical>加载中...</van-loading>
              <!-- 无数据时 -->
              <van-empty v-else-if="announceList.length === 0" description="暂无公告" />
              <!-- 列表内容 -->
              <template v-else>
                <div v-for="(item, index) in announceList" :key="index" class="sale-announce-item" @click="goToAnnounceDetail(item.id)">
                  <div class="announce-info">
                    <div class="announce-title">{{ item.title }}</div>
                    <div class="announce-time">{{ item.formattedTime }}</div>
                  </div>
                </div>
              </template>
            </div>
          </van-tab>
        </van-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed,watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getNFTDetail, getPurchaseRequestsByNftId } from '@/api/market';
import { getAnnounceList } from '@/api/market';
import dayjs from 'dayjs';
import { Toast } from 'vant';
import SortIndicator from '@/components/SortIndicator.vue';

const currentSort = ref({ key: '', order: '' });
const route = useRoute();
const router = useRouter();
const nftId = route.query.id;
const activeTab = ref(0);
const isSwitchOn = ref('left')//left代表寄售，right代表求购;

const tab = route.query.tab;

const issueCount = ref(0);
const circulationCount = ref(0);
const saleList = ref([]);
const purchaseList = ref([]); // 求购列表
const isReachBottom = ref(false);
const imageUrl = ref('');
const name = ref('');
const loading = ref(false); // 加载状态
const announceList = ref([]);
// 监听标签切换
const onTabChange = (index) => {
  // 0是市场价格，1是相关公告
  if (index === 1 && announceList.value.length === 0) {
    fetchAnnounceList();
  }
};
// 监听路由变化，更新 activeTab
watch(route, (newRoute) => {
  if (newRoute.query.activeTab) {
    activeTab.value = parseInt(newRoute.query.activeTab, 10);
  }
}, { immediate: true });

// 计算当前显示的数据列表
const currentDataList = computed(() => {
  return isSwitchOn.value === 'left' ? saleList.value : purchaseList.value;
});

// 切换寄售/求购
const toggleSwitch = async (side) => {
  isSwitchOn.value = side;

  // 切换到求购且求购数据为空时，加载求购数据
  if (side === 'right' && purchaseList.value.length === 0) {
    await loadPurchaseData();
  }
  // 切换到寄售且寄售数据为空时，加载寄售数据（初始化时已加载，此处可省略）
};
const fetchAnnounceList = async () => {
  loading.value = true;
  try {
  const response = await getAnnounceList(nftId);
    if (response) {
      announceList.value = response.map(item => ({
        id: item.id,
        title: item.title,
        startTime: item.startTime, // 保留原始时间用于排序
        formattedTime: formatTime(item.startTime) // 添加格式化后的时间
      }));
    } else {
      announceList.value = [];
    }
  } catch (error) {
    Toast.error('获取失败，请稍后再试');
    console.error('公告数据格式错误:', error);
  } finally {
    loading.value = false;
  }
};
const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD');
};
const loadData = async () => {
  loading.value = true; // 开始加载
  try {
    const response = await getNFTDetail(nftId);
    imageUrl.value = require(`@/${response.imageUrl}`);
    name.value = response.name;
    issueCount.value = response.issueCount;
    circulationCount.value = response.circulationCount;
    // 过滤掉价格不大于0的项
    saleList.value = response.instances
      .filter(item => item.price > 0) // 新增过滤条件
      .map(item => ({
        name: response.name,
        id: item.id,
        price: item.price,
        lockExpiry: item.lockExpiry // 添加锁定过期时间
      }))
      .sort((a, b) => parseFloat(a.price) - parseFloat(b.price));
    isReachBottom.value = true;
  } catch (error) {
    console.error('加载数据失败:', error);
    saleList.value = [];
  } finally {
    loading.value = false;
  }
};
const loadPurchaseData = async () => {
  loading.value = true;
  try {
    const response = await getPurchaseRequestsByNftId(nftId);

    // 正确判断方式
    if (response) {
      purchaseList.value = response
        .filter(item => item.price > 0) // 新增过滤条件
        .map(item => ({
          price: item.price
        }))
        .sort((a, b) => b.price - a.price);

    } else {
      console.error('数据格式错误:', response);
      purchaseList.value = [];
    }
  } catch (err) {
    console.error('请求失败:', err);
    purchaseList.value = [];
  } finally {
    loading.value = false;
  }
};
const handleSortChange = (sort) => {
  currentSort.value = sort;
  
  if (sort.key === 'price') {
    saleList.value.sort((a, b) => {
      const priceA = parseFloat(a.price);
      const priceB = parseFloat(b.price);
      return sort.order === 'asc' ? priceA - priceB : priceB - priceA;
    });
  } else if (sort.key === 'id') {
    saleList.value.sort((a, b) => {
      return sort.order === 'asc' ? a.id - b.id : b.id - a.id;
    });
  }
};


const goBack = () => {
  // 从当前路由中获取tab参数
  router.push({
    path: '/market',
    query: { tab:tab|| 'all' }
  });
};
const goToSaleDetail = (instanceNumber) => {
  router.push({
    path: `/saleDetail/${nftId}/${instanceNumber}`,
    query: { from: 'market' }  // 添加来源标识
  });
};
const goToAnnounceDetail = (announceId) => {
  router.push({
    path: `/announceDetail/${nftId}/${announceId}`,
  });
};
const goToSellPurchaseRequests = (item) => {
  router.push({
    path: '/sellPurchaseRequests',
    query: {
      price: item.price, // 传递价格参数
      name: name.value, // 传递名称参数
      imageUrl: imageUrl.value, // 传递图片参数
      nftId: nftId, // 传递nftId参数
      id: item.id, // 传递编号参数
      tab: tab
    },
  });
};
const handleInitiatePurchase = () => {
  router.push({
    path: '/launchPurchaseRequests',
    query: {
      highestPrice: purchaseList.value.length > 0 ? purchaseList.value[0].price : 0, // 当前最高求购价
      name: name.value, // 藏品名称
      imageUrl: imageUrl.value, // 图片 URL
      nftId: nftId, // NFT ID
    },
  });
};
// 监听路由变化
watch(() => route.query.isSwitchOn, (newVal) => {
  if (newVal === 'right') {
    isSwitchOn.value = 'right';
    loadPurchaseData();
  }
}, { immediate: true });
onMounted(() => {
  // 如果从公告详情页返回，设置 activeTab 为 query 中的值
  if (route.query.activeTab) {
    activeTab.value = parseInt(route.query.activeTab, 10);
    fetchAnnounceList();
  }
  // 加载数据
  loadData();
});
</script>


<style scoped>
.container {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
  overflow-y: auto;
  /* 允许垂直滚动 */
  user-select: none;
}

.background-image {
  position: relative;
  width: 100%;
  height: 30%;
  /* 图片占据页面顶部的 30% */
  background-size: cover;
  background-position: center;
  z-index: 1;
  /* 图片在内容下方 */
}

/* 模糊图片的最下方 10% */
.background-image::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 4%;
  /* 设置模糊层的高度为背景图片的16% */
  background: inherit;
  /* 继承背景图片 */
  filter: blur(8px);
  /* 设置模糊效果 */
  pointer-events: none;
  /* 确保点击事件可以穿透到背景图片 */
}

.header {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  padding: 10px;
  /* 添加一些内边距 */
  z-index: 10;
  /* 确保 header 在图片和模糊层上方 */
}

.arrow-container {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  /* 圆形背景的宽度 */
  height: 30px;
  /* 圆形背景的高度 */
  background-color: rgba(0, 0, 0, 0.5);
  /* 浅黑色背景 */
  border-radius: 50%;
  /* 圆形 */
  cursor: pointer;
  /* 鼠标悬停时显示为手型 */
}

.left-arrow {
  font-size: 20px;
  /* 箭头大小 */
  color: white;
  /* 箭头颜色 */
}

/* 新图片容器 */
.overlay-image-container {
  position: absolute;
  bottom: -10%;
  /* 略微超出背景图的底部 */
  left: 50%;
  /* 横向居中 */
  transform: translateX(-50%);
  /* 横向居中 */
  width: 33%;
  /* 宽度为屏幕宽度的 33% */
  z-index: 5;
  /* 确保新图片在内容上方 */
  text-align: center;
  /* 文字居中 */
}

/* 新图片样式 */
.overlay-image {
  width: 100%;
  height: auto;
  border-radius: 12px;
  /* 圆角 */
  border: 4px solid transparent;
  /* 边框 */
  background: linear-gradient(white, white) padding-box,
    linear-gradient(to bottom, gray, white) border-box;
  /* 渐变边框 */
}

/* 图片名称 */

.image-name {
  display: flex;
  justify-content: center;
  /* 横向居中 */
  align-items: center;
  font-size: 20px;
  /* 字体大小 */
  font-weight: bold;
  /* 加粗 */
  margin-top: 12px;
  /* 与图片的间距 */
  color: #333;
  /* 字体颜色 */
}

/* 发行和流通信息容器 */
.info-container {
  display: flex;
  justify-content: center;
  /* 横向居中 */
  gap: 10px;
  /* 两个信息项之间的间距 */
  margin-top: 10px;
  /* 与名称的间距 */
}

/* 单个信息项 */
.info-item {
  display: flex;
  align-items: center;
}

/* 标签样式（发行、流通） */
.label {
  background-color: rgb(236, 220, 73);
  /* 黄色背景 */
  padding: 2px 3px;
  /* 内边距 */
  border-radius: 1px;
  /* 圆角 */
  font-size: 10px;
  /* 字体大小 */
  color: #333;
  /* 字体颜色 */
}

/* 值样式 */
.value {
  background-color: #f0f0f0;
  /* 浅灰色背景 */
  padding: 2px 3px;
  /* 内边距 */
  border-radius: 4px;
  /* 圆角 */
  font-size: 10px;
  /* 字体大小 */
  color: #333;
  /* 字体颜色 */
  margin-left: 4px;
  /* 与标签的间距 */
}

/* 内容区域 */
.content {
  position: relative;
  z-index: 3;
  /* 内容在图片和模糊层上方 */
  padding: 2px;
  /* 减小顶部间距 */
  background: linear-gradient(to bottom, rgba(255, 255, 255, 0) 50%, rgba(255, 255, 255, 1) 100%);
  /* 渐变背景，从透明到白色 */
}


/* 选项卡标题背景样式 */
:deep(.van-tab__text) {
  background: none;
  /* 去掉背景颜色 */
}

/* 未选中时字体颜色为灰色 */
:deep(.van-tab:not(.van-tab--active)) .van-tab__text {
  color: rgb(126, 124, 124);
  /* 设置未选中时的字体颜色 */
}

/* Switch 和价格、编号的容器 */
.switch-and-info {
  display: flex;
  justify-content: space-between;
  /* 左右两端对齐 */
  align-items: center;
  /* 垂直居中 */
  padding: 6px 10px;
  /* 内边距 */
  /* 上边框 */
  user-select: none;
}

/* 自定义 Switch 容器 */
.custom-switch {
  display: flex;
  position: relative;
  /* 相对定位 */
  border: 1px solid #d9d7d7;
  /* 边框 */
  border-radius: 15px;
  /* 圆角 */
  overflow: hidden;
  /* 隐藏超出部分 */
  background-color: #eceaea;
  /* 默认背景颜色 */
}

/* 背景动画层 */
.switch-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 50%;
  /* 背景宽度为一半 */
  height: 100%;
  background-color: #fbfbfb;
  /* 灰色背景 */
  border-radius: 15px;
  /* 圆角 */
  transition: transform 0.3s ease;
  /* 动画效果 */
}

/* 背景移动到右边 */
.switch-background.move-right {
  transform: translateX(100%);
  /* 向右移动 */
}

/* Switch 项 */
.switch-item {
  flex: 1;
  /* 平分宽度 */
  text-align: center;
  /* 文字居中 */
  padding: 3px 2px;
  /* 内边距 */
  position: relative;
  /* 相对定位 */
  z-index: 1;
  /* 确保文字在背景上方 */
  transition: color 0.3s ease;
  /* 字体颜色过渡效果 */
  /* 过渡效果 */
}

/* 选中时的样式 */
.switch-item.active {
  color: rgb(22, 111, 220);
  /* 字体颜色 */
  /* 字体颜色 */
}

/* 未选中时的样式 */
.switch-item:not(.active) {
  color: rgb(95, 90, 90);
  /* 字体颜色 */
}

.loading {
  text-align: center;
  padding: 20px;
  color: #999;
}

.price-and-id {
  display: flex;
  align-items: center;
  /* 垂直居中 */
  gap: 10px;
  /* 价格和编号之间的间距 */
}

.price-content-container {
  margin-left: 6px;
  display: flex;
  align-items: center;
}

.volume-content-container {
  margin-left: 5px;
  display: flex;
  align-items: center;
}
/* 数据展示区域 */
.data-list {
  padding: 10px;
}

/* 无数据时 */
.no-data {
  text-align: center;
  color: #999;
  font-size: 14px;
  padding: 20px 0;
  margin-top: 20px;
}

/* 数据项 */
.sale-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

/* 左边：名称和编号 */
.left-info {
  display: flex;
  flex-direction: column;
  gap: 3px;
  text-align: left;
  /* 左边对齐 */
}

.sale-announce-item {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}
.purchase-button-container {
  position: fixed; /* 固定定位 */
  bottom: 0; /* 距离屏幕底部 0 */
  left: 0; /* 距离屏幕左侧 0 */
  width: 90%; /* 按钮宽度占满屏幕 */
  padding: 10px 16px; /* 内边距 */
  background-color: #ffffff; /* 背景色，避免与内容混淆 */
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1); /* 添加顶部阴影 */
  z-index: 100; /* 确保按钮在最上层 */
}

.purchase-button {
  background-color: #1989fa; /* 蓝色背景 */
  color: #ffffff; /* 白色字体 */
  font-size: 16px;
  font-weight: bold;
  border-radius: 16px;
  height: 44px;
  line-height: 44px;
}
.announce-info {
  flex-direction: row;
  align-items: center;
  width: 100%;
  /* 确保宽度填满容器 */
}

.announce-title {
  font-size: 16px;
  font-weight: bold;
  flex: 1;
  /* 允许标题占据可用空间 */
  text-align: left;
}

.announce-time {
  font-size: 16px;
  color: #817f7f;
  margin-top: 20px;
  /* 添加左边距分隔 */
  text-align: right;
  /* 时间右对齐 */
}

.name {
  font-size: 16px;
  color: #333;
}

.id {
  font-size: 14px;
  font-weight: bold;
  /* 编号加粗 */
}

.hash {
  font-weight: bold;
  /* # 加粗 */
}

.number {
  font-weight: bold;
  /* 编号加粗 */
}

/* 右边：价格 */
.right-info {
  font-size: 16px;
  font-weight: bold;
  /* 价格加粗 */
}

.price-symbol {
  font-weight: bold;
  /* ￥ 加粗 */
}

/* 新增求购项的样式 */
.purchase-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.purchase-price {
  font-size: 16px;
  color: #333;
}

.purchase-price .price-symbol {
  color: #050505;
  font-weight: bold;
}

.purchase-price .price {
  font-weight: bold;
  color: #050505;
}

/* 新增 chevron 图标的样式 */
.chevron-icon {
  margin-left: auto;
  /* 自动向右对齐 */
  color: #999;
  /* 灰色 */
  font-size: 14px;
  /* 调整大小 */
}

/* 没有更多 */
.no-more {
  text-align: center;
  color: #999;
  font-size: 14px;
  padding: 20px 0;
  margin-top: 30px;
}
.locked-tag {
  display: inline-block;
  margin-left: 8px; /* 与名称保持一定间距 */
  padding: 2px 6px; /* 内边距 */
  font-size: 12px; /* 字体大小 */
  font-weight: bold; /* 加粗字体 */
  color: #ffffff; /* 白色字体 */
  background-color: #28a745; /* 绿色背景 */
  border-radius: 4px; /* 圆角 */
}
</style>