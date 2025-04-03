<template>
  <div class="personal-center">
    <!-- 顶部渐变背景区域 -->
    <div class="profile-header">
      <div class="user-info">
        <van-image round width="80px" height="80px" :src="avatarUrl" class="avatar" 
        @error="handleAvatarError"/>
        <div class="name">{{ userProfile.username }}</div>
        <div class="wallet-address">钱包地址: {{ shortWalletAddress }}</div>
      </div>
    </div>

    <!-- 功能模块入口 -->
    <div class="function-modules">
      <div class="module" @click="goTo('orders')">
        <van-icon name="orders-o" size="24" />
        <span>我的订单</span>
      </div>
      <div class="module" @click="goTo('Wallet')">
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
            <van-icon name="search" size="20" class="search-icon" @click="showSearch = !showSearch"
              v-if="!showSearch" />
            <div class="search-container" v-else>
              <van-field v-model="searchText" placeholder="搜索藏品名称" clearable @update:model-value="handleSearch"
                class="search-field" />
              <span class="cancel-btn" @click="cancelSearch">取消</span>
            </div>
          </div>

          <div class="asset-list bottom-padding">
            <!-- 1. 加载中状态 -->
            <van-empty v-if="loading" description="加载中..." class="full-width-empty" />

            <!-- 2. 无数据状态 -->
            <van-empty v-else-if="!filteredAssets.length" description="暂无藏品" class="full-width-empty" />

            <!-- 3. 正常数据状态（保持两列布局） -->
            <template v-else>
              <div v-for="(asset, index) in filteredAssets" :key="index" class="asset-item"
                @click="showAssetDetail(asset)">
                <div class="image-container">
                  <img :src="require(`@/${asset.imageUrl}`)" class="custom-image" alt="藏品图片" />
                </div>
                <div class="asset-info">
                  <div class="asset-name">{{ asset.nftName }}</div>
                  <div class="asset-count">持有 {{ asset.count }}个</div>
                </div>
              </div>
            </template>
          </div>
        </van-tab>
        <van-tab title="出售资产">
          <!-- 出售资产内容 -->
        </van-tab>
      </van-tabs>
    </div>

    <!-- 资产详情弹出层 -->
    <van-popup v-model:show="showDetail" position="bottom" round :style="{ height: '70%' }">
      <div class="asset-detail-container">
        <div class="detail-header">
          <div class="detail-title">{{ selectedAsset?.nftName }}</div>
          <div class="detail-hold">持有: {{ selectedAsset?.count }}个</div>
          <van-icon name="cross" size="20" class="close-icon" @click="showDetail = false" />
        </div>

        <div class="asset-items-container">
          <div v-for="(item, index) in selectedAssetItems" :key="index" 
          class="asset-detail-item"
          @click="handleConsign(item)">
            <div class="item-image-container">
              <img :src="require(`@/${selectedAsset?.imageUrl}`)" class="detail-image" alt="藏品图片" />
            </div>
            <div class="item-info">
              <div class="item-id">#{{ item.instanceNumber }}</div>
              <div class="item-price-line">
                <span class="item-hold-days">持仓天数 {{ item.holdDays }}</span>
                <span class="item-purchase-price">
                  {{ item.isConsigning ? '寄售价 ' + item.salePrice : '购入价 ' + item.purchasePrice }}
                </span>
              </div>
            </div>
            <van-button type="primary" size="small" class="consign-btn" @click.stop="handleConsign(item)"
              :disabled="item.isConsigning">
              {{ item.isConsigning ? '寄售中' : '去寄售' }}
            </van-button>
          </div>
        </div>
      </div>
    </van-popup>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { getUserAssets } from '@/api/personal'; // 修改为正确的API引入
import { showToast } from 'vant';
import { useStore } from 'vuex';
import { useRouter }  from 'vue-router';

const router = useRouter();
const store = useStore();
const activeTab = ref(0);
const showSearch = ref(false);
const searchText = ref('');
const showDetail = ref(false);
const selectedAsset = ref(null);
const loading = ref(false);
const rawAssets = ref([]);
const defaultAvatar = require('@/assets/images/sorry.jpg');
// 从Vuex获取用户信息
const userProfile = computed(() => {
  const user = store.getters.getUserInfo || {};
  return {
    username: user.username || '未登录用户',
    avatar: user.avatar,
    walletAddress: user.walletAddress || ''
  };
});
const avatarUrl = computed(() => {
  try {
    return userProfile.value.avatar 
      ? require(`@/${userProfile.value.avatar}`) 
      : defaultAvatar;
  } catch (e) {
    return defaultAvatar;
  }
});
const handleAvatarError = () => {
  // 如果动态加载失败也使用默认头像
  return defaultAvatar;
};
// 格式化钱包地址显示
const shortWalletAddress = computed(() => {
  const address = userProfile.value.walletAddress;
  if (!address) return '未绑定钱包';
  return `${address.substring(0, 4)}...${address.substring(address.length - 4)}`;
});

// 获取资产数据
const fetchAssets = async () => {
  try {
    // 检查钱包地址
    if (!userProfile.value.walletAddress) {
      throw new Error('您还未登录');
    }

    loading.value = true;
    const res = await getUserAssets();
    if (res) {
      rawAssets.value = res.assets;
    }
  } catch (error) {
    showToast(error.message || '获取资产失败');
    console.error('API错误:', error);
  } finally {
    loading.value = false;
  }
};
// 获取当前选中资产的实例列表
const selectedAssetItems = computed(() => {
  if (!selectedAsset.value) return [];
  // 确保返回的是原始assets数组中的完整对象
  return rawAssets.value
    .filter(item => item.nftName === selectedAsset.value.nftName)
    .sort((a, b) => a.instanceNumber - b.instanceNumber);
});
// 取消搜索
const cancelSearch = () => {
  showSearch.value = false;
  searchText.value = '';
};
// 处理寄售操作
const handleConsign = async (item) => {
  try {
    if (item.isConsigning) {
      // 如果是已寄售的藏品，跳转到详情页
      router.push({
        path: `/saleDetail/${item.nftId}/${item.instanceNumber}`,
          query: { from: 'personal',
                    instanceId:item.instanceId
         }  // 添加来源标识
      });
    } else {
      // 未寄售的藏品，跳转到填写寄售价格页面
      router.push({
        path: '/fillConsignmentPrice',
        query: {
          nftId: item.nftId,
          nftName: item.nftName,
          instanceNumber: item.instanceNumber,
          totalSupply: item.issueCount,
          imageUrl: item.imageUrl,
          instanceId: item.instanceId
        }
      });
    }
  } catch (error) {
    showToast('操作失败: ' + error.message);
  }
};
// 分组计算持有数量
const groupedAssets = computed(() => {
  // 1. 首先检查原始数据是否为空或无效
  if (!rawAssets.value || rawAssets.value.length === 0) {
    return []; // 返回空数组而不是undefined或null
  }

  const groups = {};

  // 2. 使用可选链操作符防止意外错误
  rawAssets.value?.forEach(item => {
    // 3. 检查item是否存在且包含必要属性
    if (!item?.nftName) {
      console.warn('发现无效的NFT数据项:', item);
      return; // 跳过无效数据
    }

    if (!groups[item.nftName]) {
      groups[item.nftName] = {
        ...item, // 展开当前项的所有属性
        items: [], // 初始化空数组
        count: 0   // 初始化计数器
      };
    }
    
    // 4. 再次检查确保分组对象已正确初始化
    if (groups[item.nftName]?.items) {
      groups[item.nftName].items.push(item);
      groups[item.nftName].count++;
    }
  });

  // 5. 转换前检查groups是否为空
  return Object.keys(groups).length > 0 ? Object.values(groups) : [];
});

// 搜索过滤
const filteredAssets = computed(() => {
  if (!searchText.value) return groupedAssets.value;
  return groupedAssets.value.filter(asset =>
    asset.nftName.toLowerCase().includes(searchText.value.toLowerCase())
  );
});

// 显示详情
const showAssetDetail = (asset) => {
  selectedAsset.value = asset;
  showDetail.value = true;
};
const goTo = (name) => {
  router.push({ name })
}
// 初始化加载
onMounted(() => {
  fetchAssets();
});
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

.search-bar {
  padding: 8px 12px;
  height: 36px;
  /* 固定高度防止跳动 */
  box-sizing: border-box;
  text-align: right;
  align-items: center;
  /* 垂直居中 */
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
  line-height: 26px;
  /* 垂直居中 */
  font-size: 14px;
  padding: 0;
  /* 移除默认内边距 */
}

.cancel-btn {
  color: #1989fa;
  margin-left: 10px;
  font-size: 14px;
  cursor: pointer;
}

.asset-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  /* 两列布局 */
  gap: 12px;
  /* 图片间距 */
  padding: 12px;
  box-sizing: border-box;
}

/* 空状态全屏居中 */
.full-width-empty {
  grid-column: 1 / -1;
  /* 横跨所有列 */
  width: 100%;
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
  padding-top: 100%;
  /* 保持1:1正方形 */
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
  padding-bottom: 30px;
  /* 通常50-80px足够，根据实际tabbar高度调整 */
}

/* 或者可以给整个页面容器添加 */
.personal-center {
  padding-bottom: 30px;
  /* 如果给整个页面添加，可以确保所有内容都不会被遮挡 */
}

/* 详情弹窗样式 */
.asset-detail-container {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.detail-header {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
  position: relative;
}

.detail-title {
  font-size: 18px;
  font-weight: bold;
}

.detail-hold {
  font-size: 14px;
  color: #969799;
  margin-top: 5px;
}

.close-icon {
  position: absolute;
  top: 0;
  right: 0;
}

.asset-items-container {
  flex: 1;
  overflow-y: auto;
}

.asset-detail-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f7f8fa;
}

.item-image-container {
  width: 46px;
  height: 46px;
  border-radius: 10px;
  overflow: hidden;
  margin-right: 15px;
}

.detail-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}

.item-info {
  flex: 1;
}

.item-id {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 3px;
}

/* 修改或添加以下样式 */
.item-price-line {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.item-hold-days {
  font-size: 10px;
  color: #969799;
  margin-bottom: 5px;
}

.item-purchase-price {
  font-size: 10px;
  color: #969799;
  margin-bottom: 5px;
  margin-left: 6px;

}

.consign-btn {
  width: 60px;
  height: 30px;
  border-radius: 30px;
}
</style>