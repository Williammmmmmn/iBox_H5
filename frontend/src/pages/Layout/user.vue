<template>
  <div class="personal-center">
    <!-- 顶部渐变背景区域 -->
    <div class="profile-header">
      <div class="user-info">
        <van-image
          round
          width="80px"
          height="80px"
          :src="require('@/assets/images/yb.jpg')"
          class="avatar"
        />
        <div class="name">用户名</div>
        <div class="wallet-address">钱包地址: 0x8749...741966</div>
      </div>
    </div>

    <!-- 功能模块入口 -->
    <div class="function-modules">
      <div class="module" @click="goTo('orders')">
        <van-icon name="orders-o" size="24" />
        <span>我的订单</span>
      </div>
      <div class="module" @click="goTo('wallet')">
        <van-icon name="balance-o" size="24" />
        <span>我的钱包</span>
      </div>
      <div class="module" @click="goTo('settings')">
        <van-icon name="setting-o" size="24" />
        <span>设置中心</span>
      </div>
    </div>

    <!-- 资产标签页 -->
    <div class="asset-tabs">
      <van-tabs v-model:active="activeTab">
        <van-tab title="持有资产">
          <div class="search-bar">
            <van-icon 
              name="search" 
              size="20" 
              class="search-icon"
              @click="showSearch = !showSearch"
              v-if="!showSearch"
            />
            <div class="search-container" v-else>
            <van-field
              v-model="searchText"
              placeholder="搜索藏品名称"
              clearable
              @update:model-value="handleSearch"
              class="search-field"
            />
          <span class="cancel-btn" @click="cancelSearch">取消</span>
          </div>
          </div>
          <div class="asset-list bottom-padding">
    <div 
      v-for="(asset, index) in filteredAssets" 
      :key="index" 
      class="asset-item"
    >
    <div class="image-container">
        <img 
          :src="asset.image" 
          class="custom-image"
          alt="藏品图片"
        />
      </div>
      <div class="asset-info">
        <div class="asset-name">{{ asset.name }}</div>
        <div class="asset-count">持有 {{ asset.count }}</div>
      </div>
    </div>
  </div>
        </van-tab>
        <van-tab title="出售资产">
          
          <div class="asset-list bottom-padding">
    <div 
      v-for="(asset, index) in filteredAssets" 
      :key="index" 
      class="asset-item"
    >
    <div class="image-container">
        <img 
          :src="asset.image" 
          class="custom-image"
          alt="藏品图片"
        />
      </div>
      <div class="asset-info">
        <div class="asset-name">{{ asset.name }}</div>
        <div class="asset-count">持有 {{ asset.count }}</div>
      </div>
    </div>
  </div>
        </van-tab>
      </van-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const activeTab = ref(0);
const showSearch = ref(false);
const searchText = ref('');

// 模拟资产数据
const assets = ref([
  { name: '数字艺术品1', image: require('@/assets/images/zq1.jpg'), count: 1 },
  { name: '数字艺术品2', image: require('@/assets/images/zq1.jpg'), count: 2 },
  { name: '稀有收藏品1', image: require('@/assets/images/zq1.jpg'), count: 1 },
  { name: '限量版NFT', image: require('@/assets/images/zq1.jpg'), count: 3 },
  { name: '创意作品', image: require('@/assets/images/zq1.jpg'), count: 1 },
  { name: '数字收藏品', image: require('@/assets/images/zq1.jpg'), count: 2 },
]);
const cancelSearch = () => {
  showSearch.value = false;
  searchText.value = '';
};
// 过滤资产
const filteredAssets = computed(() => {
  if (!searchText.value) return assets.value;
  return assets.value.filter(asset => 
    asset.name.includes(searchText.value)
  );
});

const handleSearch = () => {
  // 搜索逻辑已在computed中处理
};

const goTo = (page) => {
  // 跳转到对应页面
  console.log(`跳转到${page}`);
};
</script>

<style scoped>
.personal-center {
  height: 100vh;
  background-color: #ffffff;
  overflow-y: auto;
  user-select: none;
}

.profile-header {
  height: 22vh;
  background: linear-gradient(to bottom, #1e90ff, #87cefa);
  display: flex;
  justify-content: center;
  align-items: center;
  padding-top: 14px;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: white;
}

.avatar {
  margin-bottom: 10px;
  border: 3px solid white;
}

.name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 5px;
}

.wallet-address {
  font-size: 14px;
  opacity: 0.8;
}

.function-modules {
  display: flex;
  justify-content: space-around;
  background-color: #f5f5f5;
  padding: 15px 0;
  margin: 10px;
  border-radius: 10px;
}

.module {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 14px;
  color: #333;
}

.module span {
  margin-top: 5px;
}

.asset-tabs {
  margin-top: 10px;
  background-color: #fff;
}

.search-bar {
  padding: 8px 12px;
  height: 36px; /* 固定高度防止跳动 */
  box-sizing: border-box;
  text-align: right;
  align-items: center; /* 垂直居中 */
}
.search-icon {
  height: 20px;
  line-height: 20px;
  margin-top: 6px;
}
.search-container {
  display: flex;
  align-items: center;
  width: 100%;
  height: 100%;

}

.search-field {
  flex: 1;
  height: 26px;
  background-color: #f5f5f5;
  border-radius: 18px;
  padding: 0 12px;
}
.search-field :deep(.van-field__control) {
  height: 26px;
  line-height: 26px; /* 垂直居中 */
  font-size: 14px;
  padding: 0; /* 移除默认内边距 */
}
.cancel-btn {
  color: #1989fa;
  margin-left: 10px;
  font-size: 14px;
  cursor: pointer;
}
.asset-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* 两列布局 */
  gap: 12px; /* 图片间距 */
  padding: 12px;
  box-sizing: border-box;
}
.asset-item {
  display: flex;
  flex-direction: column;
  background-color: #f9f9f9;
  border-radius: 8px;
  overflow: hidden;
}
.image-container {
  position: relative;
  width: 100%;
  padding-top: 100%; /* 保持1:1正方形 */
}
.custom-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.asset-info {
  padding: 8px;
}

.asset-name {
  font-size: 14px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.asset-count {
  font-size: 12px;
  color: #666;
}
/* 添加底部内边距，根据您的tabbar高度调整 */
.bottom-padding {
  padding-bottom: 30px; /* 通常50-80px足够，根据实际tabbar高度调整 */
}

/* 或者可以给整个页面容器添加 */
.personal-center {
  padding-bottom: 30px; /* 如果给整个页面添加，可以确保所有内容都不会被遮挡 */
}
</style>