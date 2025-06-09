<template>
  <div class="container">
    <div class="scroll-container">
      <!-- 背景图片容器 -->
      <div class="background-image" :style="{ backgroundImage: `url(${imageUrl})` }">
        <!-- 新增的模糊层 -->
        <div class="background-blur"></div>
        <!-- 返回箭头 -->
        <div class="header">
          <div class="arrow-container" @click="goBack">
            <van-icon name="arrow-left" class="left-arrow" />
          </div>
        </div>

        <!-- 3D相框容器 -->
        <div class="overlay-image-container">
          <!-- 新增的底座 -->
          <div class="pedestal">
            <div class="pedestal-base">
              <div class="hexagon-top"></div>
              <div class="hexagon-decoration">
                <span class="deco-line top-left"></span>
                <span class="deco-line top-right"></span>
                <span class="deco-line right"></span>
                <span class="deco-line bottom-right"></span>
                <span class="deco-line bottom-left"></span>
                <span class="deco-line left"></span>
              </div>
            </div>
          </div>
          <!-- 3D相框容器 -->
          <div class="photo-frame">
            <div class="frame-inner">
              <img :src="imageUrl" class="overlay-image" />
            </div>
            <!-- 新增的阴影 -->
            <div class="frame-shadow"></div>
          </div>
        </div>
      </div>

      <!-- 修改后的深色背景区域 -->
      <div class="dark-background">
        <!-- 图片名称 -->
        <div class="image-name">
          {{ instanceNumber? `${name}#${instanceNumber}` : name }}
        </div>

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
        <!-- 新增的藏品故事部分 -->
        <div class="story-section">
          <div class="story-title">
            <span class="line"></span>
            <span class="text">藏品故事</span>
            <span class="line"></span>
          </div>
          <div class="story-content">
            {{ storyInfo }}
          </div>
        </div>
      </div>

      <!-- 底部白色区域 -->
      <div class="bottom-sheet-container">
        <div class="bottom-sheet">
          <div class="white-top"></div>
          <van-tabs v-model="activeTab" class="tabs" @change="onTabChange">
            <van-tab :title="isPersonalView ? '管理' : '购买'">
              <div class="content">
                <div class="price-section" :class="{ 'personal-view': isPersonalView }">
                  <div class="left" v-if="!isPersonalView && instanceNumber">
                    <div class="price">￥{{ price }}</div>
                    <div class="market-list" @click="goBack">
                      市场列表
                      <van-icon name="arrow" class="chevron" />
                    </div>
                  </div>
                  <van-button 
                    type="primary" 
                    class="buy-button" 
                    :style="{ width: !instanceNumber ? '100%' : '208px' }" 
                    @click="handleAction"
                    :disabled="!isPersonalView && (isOwner || consignmentStatus === 'completed' || isLocked || saleStatus === '已结束')"
                    :class="{ 
                      'disabled-button': consignmentStatus === 'completed' || isLocked || saleStatus === '已结束',
                      'locked-button': isLocked
                    }"
                  >
                    {{ isLocked ? '已锁定' : (isPersonalView ? '取消寄售' : saleStatus === '已结束' ? '已结束' : '立即购买') }}
                  </van-button>
                </div>
              </div>
            </van-tab>
            <van-tab title="相关公告">
              <div class="announce-content">
                <!-- 加载中提示 -->
                <van-loading v-if="announceLoading" size="24px" vertical>加载中...</van-loading>
                <!-- 无数据时 -->
                <van-empty v-else-if="announceList.length === 0" description="暂无公告" />
                <!-- 列表内容 -->
                <template v-else>
                  <div 
                    v-for="(item, index) in announceList" 
                    :key="index" 
                    class="announce-item" 
                    @click="goToAnnounceDetail(item.id)"
                  >
                    <div class="announce-info">
                      <div class="announce-title">{{ item.title }}</div>
                      <div class="announce-time">{{ item.formattedTime }}</div>
                    </div>
                    <div class="announce-divider"></div>
                  </div>
                </template>
              </div>
            </van-tab>
          </van-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MySaleDetail',
  data() {
    return {
      activeTab: 0,
      announceList: [],
      announceLoading: false
    }
  }
}
</script>

<script setup>
import { useRouter, useRoute } from 'vue-router';
import { getSaleDetail,getAnnounceList } from '@/api/market';
import { ref, onMounted, computed } from 'vue';
import { showConfirmDialog } from 'vant';
import { showToast } from 'vant';
import { useStore } from 'vuex';
import { cancelSale } from '@/api/personal';
import { lockNFTInstance } from '@/api/buyLock';
import dayjs from 'dayjs';

const router = useRouter();
const route = useRoute();
const store = useStore();
const nftId = route.query.nftId;
const instanceNumber = route.query.instanceNumber;
const imageUrl = ref('');
const name = ref('');
const issueCount = ref(0);
const circulationCount = ref(0);
const storyInfo = ref('');
const price = ref(0);
const instanceId = ref(0);
const ownerAddress = ref('');
const consignmentStatus = ref('');
const isLocked = ref(false);
const announceList = ref([]);
const announceLoading = ref(false);
const saleStatus = ref(''); // 寄售状态(home)


const userProfile = computed(() => {
  const user = store.getters.getUserInfo || {};
  return {
    wallet_Address: user.walletAddress || ''
  };
});


const walletAddress = userProfile.value.wallet_Address;
// 计算属性判断是否来自个人中心
const isPersonalView = computed(() => {
  return route.query.from === 'personal';
});
// 计算属性判断是否是卖家本人
const isOwner = computed(() => {
  return walletAddress === ownerAddress.value;
});

// 新增方法：获取公告列表
const fetchAnnounceList = async () => {
  announceLoading.value = true;
  try {
    const response = await getAnnounceList(nftId);
    if (response) {
      announceList.value = response.map(item => ({
        id: item.id,
        title: item.title,
        startTime: item.startTime,
        formattedTime: formatTime(item.startTime)
      }));
    } else {
      announceList.value = [];
    }
  } catch (error) {
    showToast.error(error.message || '获取公告列表失败');
  } finally {
    announceLoading.value = false;
  }
};
// 新增方法：标签切换
const onTabChange = (index) => {
  if (index === 1 && announceList.value.length === 0) {
    fetchAnnounceList();
  }
};

// 新增方法：跳转到公告详情
const goToAnnounceDetail = (announceId) => {
  router.push({
    path: `/announceDetail/${nftId}/${announceId}`,
  });
};
// 新增方法：格式化时间
const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD');
};

const loading = ref(false); // 加载状态
const loadData = async () => {
  loading.value = true; // 开始加载
  try {
    const response = await getSaleDetail(nftId, instanceNumber);
    console.log(response);
    imageUrl.value = require(`@/${response.nftImage}`);
    name.value = response.nftName;
    issueCount.value = response.issueCount;
    circulationCount.value = response.circulationCount;
    storyInfo.value = response.nftDescription;
    price.value = response.instancePrice;
    instanceId.value = response.instanceId;
    ownerAddress.value = response.ownerAddress;
    consignmentStatus.value = response.consignmentStatus;
    // 判断是否锁定
    isLocked.value = 
      response.lockExpiry && 
      new Date(response.lockExpiry) > new Date() &&
      response.lockedBy !== walletAddress;
  } catch (error) {
    showToast(error.message || '获取数据失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};
const handleAction = async () => {
  if (isPersonalView.value) {
    await handleCancelSale();
    return;
  }

  try {
    // 调用锁定 API
    const response = await lockNFTInstance(walletAddress, instanceId.value);
    // 成功则跳转到购买页面
    if (response.success) {
      router.push({
        path: '/buyPage',
        query: {
          nftId: nftId,
          instanceNumber: instanceNumber,
          price: price.value,
          imageUrl: imageUrl.value,
          name: name.value,
          instanceId: instanceId.value,
          lockExpiry: response.lockExpiry,
        },
      });
    }
  } catch (error) {
      showToast(error.message || '锁定失败，请稍后重试');
  }
};

const handleCancelSale = async () => {
  try {
    await showConfirmDialog({
      title: '确认取消寄售',
      message: '确认取消该藏品的寄售状态？',
      confirmButtonText: '确认取消',
      cancelButtonText: '再想想',
      confirmButtonColor: '#ff4444'
    });
    console.log(walletAddress, instanceId)
    // 调用取消寄售API
    await cancelSale(
      walletAddress,
      instanceId.value
    );

    showToast('取消寄售成功');
    setTimeout(() => router.go(-1), 1000);
  } catch (error) {
    if (error !== 'cancel') {
      showToast(error.message || '取消寄售失败，请稍后重试');
    }
  }
};

const goBack = () => {
  router.go(-1);
};

 onMounted(() => {
  const from = route.query.from;
  if (from === 'home') {
    
    imageUrl.value =   require(`@/${route.query.imageUrl}`);
    name.value = route.query.name;
    issueCount.value = Number(route.query.issueCount) || 0;
    circulationCount.value = Number(route.query.circulationCount) || 0;
    price.value = Number(route.query.price) || 0;
    storyInfo.value = route.query.description;
    saleStatus.value = route.query.status;
    // 其他数据项同理进行赋值
  } else {
    loadData();
  }
});
</script>

<style scoped>
.container {
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.92);
}

.scroll-container {
  width: 100%;
  height: 100%;
  overflow-y: auto;
  scroll-behavior: smooth;
}

/* 固定顶部图片区域 */
.fixed-image-area {
  height: 60vh;
  position: relative;
  flex-shrink: 0;
}

.background-image {
  position: relative;
  width: 100%;
  height: 60vh;
  background-size: cover;
  background-position: center;
  z-index: 1;
}
/* 新增公告相关样式 */
.announce-content {
  padding: 16px;
  min-height: 200px;
}

.announce-item {
  margin-bottom: 16px;
  background-color: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s, box-shadow 0.2s;
}

.announce-item:active {
  transform: translateY(2px);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.announce-info {
  padding: 16px;
  display: flex;
  flex-direction: column;
}

.announce-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
  line-height: 1.4;
}

.announce-time {
  font-size: 12px;
  color: #999;
  align-self: flex-end;
}

.announce-divider {
  height: 1px;
  background: linear-gradient(to right, transparent, #eee, transparent);
  margin: 0 16px;
}

/* 适配暗色背景 */
.dark-background .announce-item {
  background-color: rgba(255, 255, 255, 0.1);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.dark-background .announce-title {
  color: rgba(255, 255, 255, 0.9);
}

.dark-background .announce-time {
  color: rgba(255, 255, 255, 0.6);
}

.dark-background .announce-divider {
  background: linear-gradient(to right, transparent, rgba(255, 255, 255, 0.1), transparent);
}
/* 添加深色叠加层 */
.background-image::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.9);
  /* 半透明黑色叠加 */
  z-index: 1;
}

.background-blur {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: inherit;
  background-size: cover;
  background-position: center;
  filter: blur(6px);
  z-index: 2;
  pointer-events: none;
}

.background-image::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 30%;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.95), transparent);
}

.dark-background {
  padding: 15px;
  flex-shrink: 0;
  padding: 15px;
  padding-top: 30px;
  /* 给过渡区域留出空间 */
  margin-top: -10px;
  /* 向上偏移覆盖过渡区域 */
  position: relative;
  z-index: 2;
}
.disabled-button {
  background-color: #d3d3d3 !important; /* 灰色背景 */
  color: #ffffff !important; /* 白色文字 */
  cursor: not-allowed; /* 禁用状态的鼠标样式 */
}
.header {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  padding: 10px;
  z-index: 10;
}

.arrow-container {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  cursor: pointer;
}

.left-arrow {
  font-size: 16px;
  color: white;
}

.overlay-image-container {
  position: absolute;
  bottom: 25%;
  left: 45%;
  transform: translateX(-50%);
  width: 66%;
  z-index: 10;
  text-align: center;
  perspective: 1500px;
}

.pedestal {
  position: absolute;
  width: 120%;
  height: 120px;
  bottom: -60px;
  left: 58%;
  transform: translateX(-50%);
  z-index: 5;
  perspective: 1000px;
  filter: drop-shadow(0 10px 15px rgba(0, 0, 0, 0.6));
}

/* 六边形顶部 */
.hexagon-top {
  position: absolute;
  width: 120%;
  height: 50px;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  background:
    linear-gradient(to bottom,
      rgba(110, 128, 187, 0.95) 0%,
      rgba(51, 58, 61, 0.95) 100%);
  clip-path: polygon(25% 0%, 75% 0%, 100% 50%, 75% 100%, 25% 100%, 0% 50%);
  box-shadow:
    0 8px 20px rgba(0, 0, 0, 0.5),
    inset 0 0 15px rgba(40, 40, 40, 0.4);
  z-index: 6;
  border: 1px solid rgba(18, 17, 17, 0.6);
}



/* 底部装饰线 */
.hexagon-top::after {
  content: '';
  position: absolute;
  width: 100%;
  height: 3px;
  bottom: 8px;
  left: 0;
  background: linear-gradient(to right,
      transparent 0%,
      rgba(244, 248, 16, 0.12) 20%,
      rgba(79, 9, 243, 0.7) 50%,
      rgba(244, 248, 16, 0.12) 80%,
      transparent 100%);
  border-radius: 2px;
  box-shadow: 0 0 12px rgba(11, 11, 11, 0.5);
  z-index: 7;
  clip-path: polygon(25% 0%, 75% 0%, 100% 50%, 75% 100%, 25% 100%, 0% 50%);
}

/* 六边形装饰线容器 */
.hexagon-decoration {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  pointer-events: none;
}

/* 装饰线基础样式 */
.deco-line {
  position: absolute;
  background: linear-gradient(to right,
      transparent 0%,
      rgba(245, 196, 37, 0.6) 50%,
      transparent 100%);
  height: 1px;
  width: 100%;
  box-shadow: 0 0 3px rgba(245, 196, 37, 0.4);
}

/* 定位每条装饰线 */
.deco-line.top-left {
  top: 0;
  left: 0;
  width: 50%;
  transform: rotate(-30deg);
  transform-origin: left center;
}

.deco-line.top-right {
  top: 0;
  right: 0;
  width: 50%;
  transform: rotate(30deg);
  transform-origin: right center;
}

.deco-line.right {
  top: 50%;
  right: 0;
  height: 100%;
  width: 1px;
  background: linear-gradient(to bottom,
      transparent 0%,
      rgba(245, 196, 37, 0.6) 50%,
      transparent 100%);
  transform: translateY(-50%);
}

.deco-line.bottom-right {
  bottom: 0;
  right: 0;
  width: 50%;
  transform: rotate(-30deg);
  transform-origin: right center;
}

.deco-line.bottom-left {
  bottom: 0;
  left: 0;
  width: 50%;
  transform: rotate(30deg);
  transform-origin: left center;
}

.deco-line.left {
  top: 50%;
  left: 0;
  height: 100%;
  width: 1px;
  background: linear-gradient(to bottom,
      transparent 0%,
      rgba(245, 196, 37, 0.6) 50%,
      transparent 100%);
  transform: translateY(-50%);
}

/* 3D相框 */
.photo-frame {
  position: relative;
  width: 100%;
  padding: 5%;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 16px;
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow:
    0 0 20px rgba(0, 0, 0, 0.2),
    0 0 0 1px rgba(255, 255, 255, 0.2);
  transform-style: preserve-3d;
  animation: rotateFrame 8s ease-in-out infinite;
  z-index: 10;
}

.frame-inner {
  position: relative;
  padding: 4px;
  background: linear-gradient(135deg,
      #e6e6e6 0%,
      #ffffff 25%,
      #e6e6e6 50%,
      #ffffff 75%,
      #e6e6e6 100%);
  border-radius: 10px;
  box-shadow:
    inset 0 0 10px rgba(0, 0, 0, 0.2),
    0 2px 10px rgba(0, 0, 0, 0.3);
  transform: translateZ(15px);
}

/* 新增阴影效果 */
.frame-shadow {
  position: absolute;
  width: 90%;
  height: 9px;
  bottom: -38px;
  left: 5%;
  background: radial-gradient(ellipse at center, rgba(0, 0, 0, 0.9) 0%, rgba(0, 0, 0, 0.6) 70%);
  border-radius: 50%;
  filter: blur(5px);
  transform: rotateX(75deg) scale(0.9);
  opacity: 0.9;
  z-index: -1;
  animation: shadowRotate 8s ease-in-out infinite;
}

@keyframes shadowRotate {

  0%,
  100% {
    transform: rotateX(75deg) scale(0.9) translateX(0);
  }

  25% {
    transform: rotateX(75deg) scale(0.9) translateX(10px);
  }

  50% {
    transform: rotateX(75deg) scale(0.9) translateX(0);
  }

  75% {
    transform: rotateX(75deg) scale(0.9) translateX(-10px);
  }
}

.overlay-image {
  width: 100%;
  height: auto;
  display: block;
  border-radius: 8px;
  border: 4px solid transparent;
  background:
    linear-gradient(white, white) padding-box,
    linear-gradient(135deg,
      #d1d1d1 0%,
      #ffffff 20%,
      #d1d1d1 40%,
      #ffffff 60%,
      #d1d1d1 80%,
      #ffffff 100%) border-box;
  box-shadow:
    inset 0 0 15px rgba(0, 0, 0, 0.2),
    0 0 10px rgba(255, 255, 255, 0.8);
  background-size: 200% 200%;
  animation: metalShine 3s linear infinite;
}

/* 文字颜色修改 */
.image-name {
  display: flex;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
  margin-top: 0;
  color: white;
  /* 改为白色 */
}

.info-container {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
}

.info-item {
  display: flex;
  align-items: center;
}

.label {
  background-color: rgb(238, 231, 167);
  padding: 2px 3px;
  border-radius: 3px;
  font-size: 10px;
  color: #333;
}

.value {
  background-color: rgba(255, 255, 255, 0.3);
  /* 半透明白色 */
  padding: 2px 3px;
  border-radius: 4px;
  font-size: 10px;
  color: white;
  /* 改为白色 */
  margin-left: 4px;
}

/* 藏品故事样式 */
.story-section {
  margin-top: 20px;
  padding: 0 15px;
}

.story-title {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 15px;
}

.story-title .line {
  width: 20%;
  height: 1px;
  background: rgba(255, 255, 255, 0.3);
}

.story-title .text {
  padding: 0 8px;
  font-size: 20px;
  font-weight: bold;
  color: white;
}

.story-content p {
  font-size: 14px;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.8);
  text-align: justify;
  /* 两端对齐 */
  text-justify: inter-ideograph;
  /* 中文两端对齐优化 */
  hyphens: auto;
  margin: 0;
  padding: 0 5px;
}

.story-content {
  font-size: 14px;
  line-height: 1.6;
  color: rgba(255, 255, 255, 0.8);
  text-align: center;
  padding: 0 10px;
}

/* 新增底部容器 */

/* 底部白色区域样式 */
.bottom-sheet {
  position: relative;
  background: white;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
  padding: 16px;
  padding-top: 2px;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  /* margin-top: auto;  */
  flex-shrink: 0;
}

.white-top {
  position: absolute;
  top: -1%;
  left: 50%;
  transform: translateX(-50%);
  width: 50px;
  height: 4px;
  background: rgb(61, 235, 168);
  border-radius: 2px;
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

.tabs {
  margin-bottom: 10px;
}

.price-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  /* 改为居中对齐 */
  align-items: flex-end;
  margin-top: 18px;
  height: 36px;
  /* 与按钮同高 */
}

/* 个人中心视图下的价格区域 */
.price-section.personal-view {
  justify-content: center;
  /* 个人中心视图时居中 */
}

.left {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
  /* 继承父级高度 */
}

.price {
  font-size: 19px;
  font-weight: bold;
  color: #161616;
  line-height: 1.2;
  /* 调整行高 */
}

.market-list {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  line-height: 1.2;
  /* 调整行高 */
}

.chevron {
  margin-left: 4px;
}

.buy-button {
  width: 208px;
  height: 36px;
  border-radius: 18px;
  font-weight: bold;
  flex-shrink: 0;
  /* 防止按钮被压缩 */
}
/* 提高选择器优先级 */
.buy-button.full-width {
  width: 96% !important;
  margin: 0 auto;
}
/* 个人中心视图下的按钮样式 */
.price-section.personal-view .buy-button {
  width: 80%;
  /* 占据屏幕80%宽度 */
  margin: 0 auto;
  /* 水平居中 */
}

/* 取消寄售按钮特殊样式 */
.price-section.personal-view .buy-button {
  background-color: #3aa1fc;
  /* 红色表示警告操作 */
}

/* 动画保持不变 */
@keyframes metalShine {
  0% {
    background-position: 0% 0%;
  }

  100% {
    background-position: 200% 200%;
  }
}

@keyframes rotateFrame {

  0%,
  100% {
    transform: rotateY(0deg) rotateX(5deg);
  }

  25% {
    transform: rotateY(-20deg) rotateX(5deg);
  }

  50% {
    transform: rotateY(0deg) rotateX(-5deg);
  }

  75% {
    transform: rotateY(20deg) rotateX(5deg);
  }
}

.locked-button {
  background-color: #ccc !important;
  border-color: #ccc !important;
}
</style>