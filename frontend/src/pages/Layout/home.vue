<template>
  <div class="home">
    <!-- 导航栏 -->
    <van-nav-bar class="top">
      <template #left>
        <img src="@/assets/images/iBox.jpg" alt="iBox Logo" class="nav-logo"/>
      </template>
      <template #right>
        <van-search v-model="searchValue" placeholder="搜索商品" class="nav-search" shape="round"
                    background="transparent"
                    @click-input="handleSearchClick">
          <template #left-icon>
          </template>
          <template #right-icon>
            <van-icon name="search" size="20" @click="handleSearchClick"/>
          </template>
        </van-search>
      </template>
    </van-nav-bar>

    <!-- 轮播图和公告栏模块 -->
    <div class="banner-notice-module">
      <!-- 轮播图 -->
      <van-swipe :autoplay="3000" class="swipe-container">
        <van-swipe-item v-for="(image, index) in snakeYearImages" :key="index">
          <img :src="image" alt="轮播图" class="snake-year-image"/>
        </van-swipe-item>
      </van-swipe>
      <!-- 公告栏 -->
      <div class="notice-bar-container">
        <div class="notice-label">公告</div>
        <div class="notice-content">
          <div class="notice-item"
               :class="{ active: index === noticeIndex, leave: index === (noticeIndex - 1 + notices.length) % notices.length }"
               v-for="(notice, index) in notices" :key="index">
            {{ notice }}
          </div>
        </div>
        <van-icon name="bars" size="16" @click="goToNoticeDetail" class="notice-icon"/>
      </div>
    </div>

    <!-- 功能项目 -->
    <div class="function-module">
      <div class="scroll-container" ref="scrollContainer" @scroll="handleScroll">
        <div class="scroll-content">
          <div class="grid-container">
            <van-grid :column-num="6" :border="false">
              <van-grid-item v-for="(item, index) in gridItems" :key="index">
                <img :src="item.image" alt="功能项目"/>
                <span>{{ item.text }}</span>
              </van-grid-item>
            </van-grid>
          </div>
        </div>
      </div>
      <div class="indicator-container">
        <div class="indicator-line">
          <div class="indicator" :style="indicatorStyle"></div>
        </div>
      </div>
    </div>

    <!-- 社区模块 -->
    <div class="tree-component-container">
      <div class="community">
        <div class="community-container-two">
          <!-- 左侧轮播图 -->
          <div class="swipe-container-two">
            <van-swipe :autoplay="3000" class="swipe">
              <van-swipe-item v-for="(image, index) in communityImages" :key="index">
                <img :src="image" alt="社区轮播图" class="swipe-image"/>
              </van-swipe-item>
            </van-swipe>
          </div>
        </div>
      </div>
      <div class="content-container">
        <!-- 市场 -->
        <div class="content-item">
          <div class="text">
            <p class="title">市场</p>
            <p class="description">板块涨幅一览无余</p>
          </div>
          <img src="@/assets/images/new.png" alt="市场" class="item-image"/>
        </div>
        <!-- 今日热榜 -->
        <div class="content-item">
          <div class="text">
            <p class="title">今日热榜</p>
            <p class="description">每天24点更新</p>
          </div>
          <img src="@/assets/images/have.png" alt="今日热榜" class="item-image"/>
        </div>
        <!-- 数字原生 -->
        <div class="content-item">
          <div class="text">
            <p class="title">数字原生</p>
            <p class="description">下载链接</p>
          </div>
          <img src="@/assets/images/yu.png" alt="数字原生" class="item-image"/>
        </div>
      </div>
    </div>

    <!-- 发售模块 -->
    <div class="sell">
      <van-cell title="发售记录" :border="false" class="custom-cell"/>

      <div class="dropdown-container">
        <!-- 类别下拉框 -->
        <div class="dropdown-button-category" @click="showCategoryPicker = true">
          <span>{{ '类别' }}</span>
          <van-icon :name="showCategoryPicker ? 'arrow-up' : 'arrow-down'" size="8"/>
        </div>
        <van-popup v-model:show="showCategoryPicker" round position="top">
          <van-picker :columns="categoryOptions" @cancel="showCategoryPicker = false" @confirm="onCategoryConfirm"/>
        </van-popup>

        <!-- 售卖状态下拉框 -->
        <div class="dropdown-button-sell" @click="showStatusPicker = true">
          <span>{{ selectedStatus || '售卖状态' }}</span>
          <van-icon :name="showStatusPicker ? 'arrow-up' : 'arrow-down'" size="8"/>
        </div>
        <van-popup v-model:show="showStatusPicker" round position="top">
          <van-picker :columns="statusOptions" @cancel="showStatusPicker = false" @confirm="onStatusConfirm"/>
        </van-popup>

        <div class="price-content-container">
          <div class="text-container">
            <span class="text">价格排序</span>
            <div class="triangle-container">
              <span class="triangle triangle-up" :class="{ 'active': isAsc }"></span>
              <span class="triangle triangle-down" :class="{ 'active': !isAsc }"></span>
            </div>
          </div>
        </div>
        <div class="time-content-container">
          <div class="text-container">
            <span class="text">时间倒序</span>
            <div class="triangle-container">
              <span class="triangle triangle-up" :class="{ 'active': isDesc }"></span>
              <span class="triangle triangle-down" :class="{ 'active': !isDesc }"></span>
            </div>
          </div>
        </div>
      </div>

      <!-- 发售记录内容 -->
      <div class="record-container">
        <div class="card">
          <img src="@/assets/images/saveMom.jpg" alt="沉香救母" class="card-image"/>
          <div class="card-content">
            <h3 class="card-title">沉香救母</h3>
            <div class="edition-container"> <!-- 新增容器 -->
              <p class="card-edition-sell">发行 50000</p>
              <p class="card-edition-tans">流通 12139</p>
            </div>
            <p class="card-price">¥9.9</p>
          </div>
        </div>
        <div class="card">
          <img :src="require('@/assets/images/greatChina.jpg')" alt="盛世华夏" class="card-image">
          <div class="card-content">
            <h3 class="card-title">盛世华夏</h3>
            <div class="edition-container"> <!-- 新增容器 -->
              <p class="card-edition-sell">发行 50000</p>
              <p class="card-edition-tans">流通 10539</p>
            </div>
            <p class="card-price">¥1.99</p>
          </div>
        </div>
      </div>
      <!-- //增加一块空白 -->
      <div class="blank"></div>
    </div>

  </div>
</template>
<script>
export default {
  name: 'MyHome'
}
</script>

<script setup>
import {ref, onMounted, computed} from 'vue';
import {useRouter} from 'vue-router';

const router = useRouter();
const scrollContainer = ref(null);
const scrollPosition = ref(0);
const containerWidth = ref(0);
const contentWidth = ref(0);
const searchValue = ref('');
const snakeYearImages = ref([
  require('@/assets/images/snake-year-1.png'),
  require('@/assets/images/snake-year-2.png'),
]);
const notices = ref([
  '《身份识别码》功能操作手册',
  '系统升级通知',
  '新用户注册福利',
  '春节活动预告',
]);
const noticeIndex = ref(0);
const communityImages = ref([
  require('@/assets/images/community.jpg'),
  require('@/assets/images/peopleCount.jpg'),
]);
// const records = ref([
//   {
//     title: '沉香救母',
//     issued: '50000',
//     circulation: '10539',
//     status: '暂停售卖',
//     price: '￥9.9',
//     imageUrl: require('@/assets/images/community.jpg') // 使用 require
//   },
//   {
//     title: '盛世华夏',
//     issued: '50000',
//     circulation: '12190',
//     status: '已售罄',
//     price: '￥1.99',
//     imageUrl: require('@/assets/images/community.jpg') // 使用 require

//   },
// ]);
const showCategoryPicker = ref(false);
const showStatusPicker = ref(false);
const selectedCategory = ref('');
const selectedStatus = ref('');
const categoryOptions = [
  {text: '杭州', value: '杭州'},
  {text: '宁波', value: '宁波'},
  {text: '温州', value: '温州'},
  {text: '绍兴', value: '绍兴'},
  {text: '湖州', value: '湖州'},
];
const statusOptions = [
  {text: '全部', value: '全部'},
  {text: '进行中', value: '进行中'},
  {text: '已结束', value: '已结束'},
];
const isAsc = ref(true);
const isDesc = ref(false);

const indicatorStyle = computed(() => {
  const scrollPercentage = scrollPosition.value / (contentWidth.value - containerWidth.value);
  return {
    left: `${scrollPercentage * 50}%`,
  };
});

const handleSearchClick = () => {
  router.push('/search');
};

const goToNoticeDetail = () => {
  router.push('/notice');
};

const startNoticeCarousel = () => {
  setInterval(() => {
    noticeIndex.value = (noticeIndex.value + 1) % notices.value.length;
  }, 3000);
};

const onCategoryConfirm = (value) => {
  selectedCategory.value = value;
  showCategoryPicker.value = false;
};

const onStatusConfirm = (value) => {
  selectedStatus.value = value;
  showStatusPicker.value = false;
};


const handleScroll = (event) => {
  scrollPosition.value = event.target.scrollLeft;
};

onMounted(() => {
  startNoticeCarousel();
  containerWidth.value = scrollContainer.value.offsetWidth;
  contentWidth.value = scrollContainer.value.scrollWidth;
});

const gridItems = [
  {image: require('@/assets/images/new.png'), text: '合成中心'},
  {image: require('@/assets/images/year.png'), text: '抽奖活动'},
  {image: require('@/assets/images/kuai.png'), text: '龙晶'},
  {image: require('@/assets/images/happy.png'), text: '在线客服'},
  {image: require('@/assets/images/have.png'), text: '识别码'},
  {image: require('@/assets/images/yu.png'), text: '星界'},
];
</script>

<style scoped>
.top {
  margin-bottom: 1%;
}

.nav-logo {
  width: 70px;
  height: auto;
  position: absolute;
  left: 0;
  user-select: none;
}

.nav-search {
  width: 226px;
}

.home {
  user-select: none;
  padding: 1%;
  background-color: #fcfcfc;
  overflow-y: auto; /* 允许垂直滚动 */
  height: 100vh; /* 设置高度为视口高度 */
}

.banner-notice-module {
  width: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

.swipe-container {
  width: 100%;
  height: 128px;
}

.snake-year-image {
  width: 100%;
  height: 100%;
  background-size: cover;
}

.notice-bar-container {
  display: flex;
  align-items: center;
  background-color: transparent;
  padding: 8px 12px;
  border-radius: 6px;
  overflow: hidden;
}

.notice-label {
  background-color: #007BFF;
  color: #fff;
  padding: 2px 4px;
  border-radius: 4px;
  font-size: 13px;
  margin-right: 8px;
  flex-shrink: 0;
}

.notice-content {
  flex-grow: 1;
  position: relative;
  height: 24px;
  overflow: hidden;
}

.notice-item {
  position: absolute;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #212121;
  transform: translateY(300%);
  transition: transform 0.5s ease-in-out;
}

.notice-item.active {
  transform: translateY(0);
}

.notice-item.leave {
  transform: translateY(300%);
}

.function-module {
  width: 100%;
  height: 102px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: 6px;
  background-color: #fff;
}

.scroll-container {
  overflow-x: auto;
  white-space: nowrap;
  width: 100%;
  position: relative;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.scroll-container::-webkit-scrollbar {
  display: none;
}

.scroll-content {
  display: inline-block;
  width: auto;
}

.grid-container {
  display: inline-flex;
  width: 100%;
}

.van-grid {
  display: flex;
  width: 100%;
  flex-wrap: nowrap;
}

.van-grid-item {
  flex: 0 0 auto;
  width: 20%;
  max-width: 20vw;
  text-align: center;
  padding: 6px;
  box-sizing: border-box;
}

.van-grid-item img {
  width: clamp(36px, 10vw, 50px);
  height: clamp(36px, 10vw, 50px);
  border-radius: 50%;
  margin-bottom: 1px;
}

.van-grid-item span {
  font-size: clamp(12px, 2.5vw, 0px);
  white-space: nowrap;
}

.indicator-container {
  width: 100%;
  padding: 0px;
  display: flex;
  justify-content: center;
}

.indicator-line {
  width: 10%;
  height: 2px;
  background-color: #ccc;
  position: relative;
  border-radius: 1px;

}

.indicator {
  width: 50%;
  height: 2px;
  background-color: green;
  position: absolute;
  transition: left 0.3s ease;
  border-radius: 1px;
}

.swipe-container-two {
  flex: 0 0 48%;
  height: 180px;
  border-radius: 12px;
  overflow: hidden;
}

.tree-component-container {
  display: flex;
  align-items: stretch;
  width: 100%;
}

.community {
  width: 46%;
  margin-top: 2%;
}

.community-container-two {
  display: flex;
  align-items: stretch;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  height: 180px;
}

.swipe-container-two {
  flex: 0 0 100%;
  height: 100%;
}

.swipe {
  width: 100%;
  height: 100%;
}

.swipe-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.content-container {
  margin-top: 2%;
  margin-left: 1.6%;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 6px
}

.content-item {
  width: 100%;
  height: 54px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(52, 51, 51, 0.1);
}

.text {
  flex: 1;
  margin-right: 10%;
}

.title {
  font-size: 14px;
  font-weight: bold;
  color: #403f3f;
  margin-left: 10%;
  margin-top: 16px;
}

.description {
  font-size: 11px;
  color: #666;
  margin-left: 10px;
  margin-bottom: 16px;
}

.item-image {
  width: clamp(36px, 10vw, 50px);
  height: clamp(36px, 10vw, 50px);
  border-radius: 50%;
  margin-right: 4%;
}

.sell {
  margin-top: 6px;
  width: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
  background-color: #f7f7f7;
  margin-left: 10px;
  border-radius: 12px;
  cursor: pointer;
  user-select: none;
  width: 46px;
  height: 18px;
  box-sizing: border-box;
}

.dropdown-button-sell {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 2px 13px;
  background-color: #f7f7f7;
  border-radius: 12px;
  cursor: pointer;
  user-select: none;
  width: 66px;
  height: 18px;
  box-sizing: border-box;
}

.dropdown-button-category span {
  font-size: 10px;
  color: #817f7f;
}

.dropdown-button-sell span {
  font-size: 10px;
  color: #817f7f;
}

.dropdown-button .van-icon {
  margin-right: 4px;
  color: #666;
}

.price-content-container {
  margin-left: 42px;
  display: flex;
  align-items: center;
}

.time-content-container {
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
  color: #817f7f;
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

.record-container {
  margin-top: 5%;
  display: flex;
  justify-content: space-between;
  gap: 16px;
}

.card {
  width: 45%;
  max-width: 300px;
  border-radius: 8px;
}

.card:first-child {
  margin-left: 4%;
  /* 左边的卡片距离屏幕左边2% */
}

.card:last-child {
  margin-right: 4%;
  /* 右边的卡片距离屏幕右边2% */
}

.card-image {
  width: 100%;
  aspect-ratio: 1 / 1;
  /* 1:1 宽高比 */
  border-radius: 8px;
}

.card-status {
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 4px 8px;
  border-radius: 4px 4px 0 0;
  font-size: 14px;
}

.card-content {
}

.card-title {
  font-size: 14px;
  margin: 7px 0 0;
}

.edition-container {
  display: flex; /* 让内容在同一行显示 */
  gap: 8px; /* 设置两个元素之间的间距 */
  margin: 2px 0; /* 上下外边距 */
}


.card-edition-sell {

  font-size: 10px;
  color: #afadad;
  margin: 0; /* 去掉默认的 margin */
  padding: 1px 2px; /* 内边距 */
  border: 0.1px solid #f1efef; /* 边框 */
  background-color: transparent; /* 透明背景 */
  border-radius: 12px; /* 圆角 */
}

.card-edition-tans {
  font-size: 10px;
  color: #afadad;
  margin: 0; /* 去掉默认的 margin */
  padding: 1px 0px; /* 内边距 */
  border: 0.1px solid #f1efef; /* 边框 */
  background-color: transparent; /* 透明背景 */
  border-radius: 12px; /* 圆角 */
}

.card-price {
  font-size: 14px;
  color: #393737;
  font-weight: bold;
}

.blank {
  width: 100%;
  height: 80px;
}
</style>