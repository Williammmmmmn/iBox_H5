<template>
  <div class="notice-page">
    <!-- 顶部导航 -->
    <van-nav-bar
      title="查看公告"
      left-arrow
      @click-left="goBack"
    />
    
    <!-- 搜索框 -->
    <van-search
      v-model="keyword"
      placeholder="请输入搜索关键词"
      shape="round"
      @search="handleSearch"
    />
    
    <!-- 公告列表 -->
    <div class="announcement-list">
      <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
        <van-list
          v-model:loading="loading"
          :finished="finished"
          finished-text="没有更多了"
          @load="onLoad"
        >
          <div 
            v-for="item in list" 
            :key="item.id" 
            class="announcement-item"
            @click="goToDetail(item.nftId, item.id)"
          >
            <div class="announcement-title">{{ item.title }}</div>
            <div class="announcement-time">
              {{ formatTime(item.createdAt) }}
            </div>
          </div>
          
          <div v-if="list.length === 0 && !loading" class="no-data">
            <van-empty description="暂无公告数据" />
          </div>
        </van-list>
      </van-pull-refresh>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { getAnnouncements } from '@/api/announcements';

const keyword = ref('');
const list = ref([]);
const loading = ref(false);
const finished = ref(false);
const refreshing = ref(false);
const pageNum = ref(1);
const pageSize = ref(10);

const router = useRouter();

// 新增时间格式化函数
const formatTime = (timeStr) => {
  return timeStr.replace('T', ' ');
};

const handleSearch = async () => {
  // 重置状态
  list.value = [];
  pageNum.value = 1;
  finished.value = false;
  loading.value = true;
  
  try {
    const res = await getAnnouncements(keyword.value, 1, pageSize.value);
    
    if (res.code === 200 && res.data) {
      list.value = res.data.list || [];
      finished.value = list.value.length >= (res.data.total || 0);
      pageNum.value = 2; // 下次加载第二页
    } else {
      finished.value = true;
    }
  } catch (error) {
    console.error('搜索公告失败:', error);
    finished.value = true;
  } finally {
    loading.value = false;
  }
};

const onLoad = async () => {
  if (refreshing.value) {
    list.value = [];
    refreshing.value = false;
  }
  
  try {
    const res = await getAnnouncements(keyword.value, pageNum.value, pageSize.value);
    
    if (res.code === 200 && res.data) {
      if (pageNum.value === 1) {
        list.value = res.data.list || [];
      } else {
        list.value = [...list.value, ...(res.data.list || [])];
      }
      
      finished.value = list.value.length >= (res.data.total || 0);
      
      if (res.data.list?.length > 0) {
        pageNum.value++;
      }
    } else {
      finished.value = true;
    }
  } catch (error) {
    console.error('加载公告失败:', error);
    finished.value = true;
  } finally {
    loading.value = false;
    refreshing.value = false;
  }
};
  // 修改跳转方法
  const goToDetail = (nftId, announceId) => {
    router.push({
      path: `/announceDetail/${nftId}/${announceId}`,
      query: {
        from: 'notice' // 添加来源标识
      }
    });
  };
  //从本页面则是直接返回首页
  const goBack = () => {
  router.push('/');
};
const onRefresh = () => {
  list.value = [];
  pageNum.value = 1;
  finished.value = false;
  loading.value = true;
  onLoad();
};
</script>

<style scoped>
/* 保持原有样式不变 */
.notice-page {
  height: 100vh;
  background-color: #f7f8fa;
  user-select: none;
}

.announcement-list {
  padding: 10px;
}

.announcement-item {
  background: #fff;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 10px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.announcement-title {
  font-family: '黑体', sans-serif;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
}

.announcement-time {
  font-size: 12px;
  color: #969799;
  text-align: right;
}

.no-data {
  padding-top: 50px;
  text-align: center;
}
</style>