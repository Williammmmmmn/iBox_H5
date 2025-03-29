<template>
  <div class="announcement-detail">
    <!-- 顶部导航栏 -->
    <van-nav-bar 
      class="top-nav" 
      :border="false" 
      left-arrow 
      @click-left="goBack" 
      title="相关公告" 
    />
    
    <!-- 加载状态 -->
    <van-loading v-if="loading" size="large" />
    
    <!-- 错误提示 -->
    <van-empty v-else-if="error" description="加载公告详情失败">
      <van-button round type="primary" @click="fetchDetail">重新加载</van-button>
    </van-empty>
    
    <!-- 内容区域 -->
    <template v-else-if="detailData">
      <div class="content-container">
        <!-- 标题 -->
        <h1 class="title">{{ detailData.title }}</h1>
        
        <!-- 发布时间和发布者 -->
        <div class="meta-info">
          <span class="time">{{ formatTime(detailData.startTime) }}</span>
        </div>
        
        <!-- 公告图片 -->
        <van-image 
          v-if="detailData.imageUrl"
          class="announce-image"
          :src="require(`@/${detailData.imageUrl}`)"
          fit="cover"
        />
        
        <!-- 固定问候语 -->
        <div class="greeting">亲爱的用户：</div>
        
        <!-- 公告内容 -->
        <div class="content">{{ detailData.content }}</div>
        <div class="publisher">{{ detailData.publisher }}</div>
        
        <!-- NFT图片 -->
        <van-image 
          v-if="detailData.nftImageUrl"
          class="nft-image"
          :src="require(`@/${detailData.nftImageUrl}`)"
          fit="cover"
        />
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getAnnounceDetail } from '@/api/market';

const route = useRoute();
const router = useRouter();
const announceId = route.params.announceId;
const nftId = route.params.nftId;
// 状态管理
const detailData = ref(null);
const loading = ref(true);
const error = ref(false);

// 格式化时间
const formatTime = (timeString) => {
  if (!timeString) return '';
  try {
    const date = new Date(timeString);
    return date.toLocaleString();
  } catch (e) {
    return timeString;
  }
};

// 获取公告详情
const fetchDetail = async () => {
  loading.value = true;
  error.value = false;
  
  try {
    const res = await getAnnounceDetail(announceId);
    
    // 检查响应结构
    if (res) {
      detailData.value = res;
    } else {
      throw new Error(res?.message || '返回数据格式不正确');
    }
  } catch (err) {
    console.error('获取公告详情失败:', err);
    error.value = true;
  } finally {
    loading.value = false;
  }
};

// 返回上一页
const goBack = () => {
  router.push({
    path:'/consignment',
    query: {
      id: nftId, // 传递 nftId
      activeTab: 1 // 设置 activeTab 为 1，对应“相关公告”标签
    }
  });
};

// 组件挂载时获取数据
onMounted(() => {
  fetchDetail();
});
</script>

<style scoped>
.announcement-detail {
  min-height: 100vh;
  background-color: #fff;
  padding-bottom: 20px;
}

.top-nav {
  background-color: #fff;
  position: sticky;
  top: 0;
  z-index: 100;
}

.content-container {
  padding: 16px;
}

.title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 12px;
  color: #333;
}

.meta-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
  font-size: 14px;
  color: #666;
}

.time {
  color: #999;
}

.announce-image,
.nft-image {
  width: 100%;
  height: auto;
  max-height: 300px;
  border-radius: 8px;
  margin: 12px 0;
}

.greeting {
  font-size: 16px;
  color: #333;
  margin: 16px 0 8px;
  font-weight: bold;
}

.content {
  font-size: 15px;
  color: #333;
  line-height: 1.8;
  margin-bottom: 20px;
  white-space: pre-line;
}
.publisher{
  font-size: 16px;
  text-align: right;
  margin-right: 36px;
}
</style>