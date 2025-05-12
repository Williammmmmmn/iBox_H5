<template>
  <div class="search-page">
    <van-nav-bar>
      <template #left>
        <van-icon name="arrow-left" size="20" @click="handleBackClick"/>
      </template>
      <template #right>
        <van-search
            v-model="searchValue"
            placeholder="搜索商品"
            class="nav-search"
            shape="round"
            background="transparent"
            @search="handleSearch"
        >
          <template #left-icon>
            <!-- 去除搜索框左边图标 -->
          </template>
          <template #right-icon>
            <van-icon name="search" size="20" @click="handleSearch"/>
          </template>
        </van-search>
      </template>
    </van-nav-bar>

    <!-- 搜索结果 -->
    <div v-if="searchResults.length > 0" class="search-results">
      <div class="result-header">
        <span class="album">专辑</span>
        <span class="count">共{{ totalResults }}个搜索结果</span>
      </div>
      
      <div class="result-grid">
        <div 
          v-for="(item, index) in searchResults" 
          :key="index" 
          class="result-item"
          @click="handleItemClick(item)"
        >
          <img :src="require('@/'+item.imageUrl)" alt="" class="item-image">
          <div class="item-name">{{ item.name }}</div>
          <div class="item-price">{{ item.minPrice || '--' }}</div>
        </div>
      </div>
    </div>

    <!-- 无结果提示 -->
    <div v-else-if="hasSearched" class="no-result">
      <van-empty description="暂无搜索结果" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { searchNFTs } from '@/api/search';

const router = useRouter();

const searchValue = ref('');
const searchResults = ref([]); // 初始化为空数组
const totalResults = ref(0);
const hasSearched = ref(false);

const handleBackClick = () => {
  router.push('/');
};

const handleSearch = async () => {
  if (!searchValue.value.trim()) return;
  
  try {
    const res = await searchNFTs(searchValue.value);
    // 直接使用 res.data，确保它是数组
    searchResults.value = Array.isArray(res.data) ? res.data : [];
    totalResults.value = searchResults.value.length;
    hasSearched.value = true;
  } catch (error) {
    console.error('搜索失败:', error);
    searchResults.value = [];
    totalResults.value = 0;
    hasSearched.value = true;
  }
};

const handleItemClick = (item) => {
  router.push(`/detail/${item.id}`);
};
</script>

<style scoped>
.search-page {
  padding: 10px;
}

.nav-search {
  width: 76vw;
}

.van-search__left-icon {
  display: none;
}

.van-search__right-icon {
  margin-left: 10px;
}

.search-results {
  padding: 15px;
}

.result-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.album {
  font-size: 18px;
  font-weight: bold;
  margin-right: 10px;
}

.count {
  font-size: 12px;
  color: #999;
}

.result-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.result-item {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.item-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
}

.item-name {
  padding: 8px;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.item-price {
  padding: 0 8px 8px;
  font-size: 14px;
  color: #ff4d4f;
}

.no-result {
  margin-top: 50px;
}
</style>