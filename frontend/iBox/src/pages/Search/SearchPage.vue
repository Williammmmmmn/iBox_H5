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
            @click-input="handleSearchClick"
        >
          <template #left-icon>
            <!-- 去除搜索框左边图标 -->
          </template>
          <template #right-icon>
            <van-icon name="search" size="20" @click="handleSearchClick"/>
          </template>
        </van-search>
      </template>
    </van-nav-bar>
  </div>
</template>

<script>
import {ref} from 'vue';
import {useRouter} from 'vue-router';

export default {
  name: 'SearchPage',
  setup() {
    const searchValue = ref('');
    const router = useRouter();

    const handleBackClick = () => {
      router.push('/'); // 返回首页
    };

    const handleSearchClick = () => {
      const searchInput = document.querySelector('.van-search__content input');
      if (searchInput) {
        searchInput.focus(); // 聚焦输入框
      }
    };

    return {
      searchValue,
      handleBackClick,
      handleSearchClick,
    };
  },
};
</script>

<style scoped>
.search-page {
  padding: 10px;
}

.nav-search {
  width: 76vw;
}

/* 隐藏左侧搜索图标 */
.van-search__left-icon {
  display: none;
}

.van-search__right-icon {
  margin-left: 10px;
}

.keyboard {
  margin-top: 20px;
  text-align: center;
}
</style>