<template>
  <div class="edit-nickname-page">
    <!-- 顶部导航 -->
    <van-nav-bar
      title="修改昵称"
      left-arrow
      right-text="保存"
      @click-left="onBack"
      @click-right="onSave"
    />

    <!-- 昵称输入框 -->
    <div class="nickname-container">
      <van-field
        v-model="newNickname"  
        label="当前昵称"
        placeholder="请输入新的昵称"
        clearable
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'; 
import { useRouter } from 'vue-router';
import { showToast } from 'vant';
import { useStore } from 'vuex';
import { updateNickname } from '@/api/setting';

const store = useStore();
const router = useRouter();

// 当前显示的昵称（从store获取，只读）
const currentNickname = computed(() => store.getters.getUserInfo?.username || '用户昵称');

// 用户输入的新昵称（本地可修改）
const newNickname = ref('');

// 初始化时设置初始值
onMounted(() => {
  newNickname.value = currentNickname.value;
});

const onBack = () => {
  router.go(-1);
};

const onSave = async() => {
  // 使用 newNickname.value 获取用户输入的新值
  if (!newNickname.value.trim()) {
    showToast('昵称不能为空');
    return;
  }
  
  try {
    await updateNickname(newNickname.value); // 传递用户输入的新值
    store.commit('setUserInfo', { 
      ...store.getters.getUserInfo, 
      username: newNickname.value 
    });
    showToast('昵称已保存');
    router.go(-1);
  } catch (error) {
    showToast('更新失败：' + error.message);
  }
};
</script>
  
  <style scoped>
  /* 页面背景颜色 */
  .edit-nickname-page {
    background-color: #f7f8fa; /* 浅灰色背景 */
    min-height: 100vh; /* 占满整个视口高度 */
    padding: 0;
    margin: 0;
    user-select: none;
  }
  
  /* 昵称输入框容器 */
  .nickname-container {
    background-color: #ffffff; /* 纯白色背景 */
    padding: 16px; /* 内边距 */
    margin: 16px; /* 外边距 */
    border-radius: 8px; /* 圆角 */
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* 添加阴影 */
  }
  </style>