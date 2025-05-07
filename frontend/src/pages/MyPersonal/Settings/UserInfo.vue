<template>
  <div class="user-info-page">
    <!-- 顶部导航 -->
    <van-nav-bar title="个人信息" left-arrow @click-left="onBack" />

    <!-- 容器 -->
    <van-cell-group class="user-info-container">
      <!-- 第一行：头像 -->
      <van-cell title="头像" @click="onAvatarClick">
        <template #value>
          <img class="avatar" :src="formattedAvatarUrl" alt="头像" />
        </template>
      </van-cell>

      <!-- 第二行：昵称 -->
      <van-cell title="昵称" is-link @click="onEditNickname">
        <template #value>
         {{username}}
        </template>
      </van-cell>

      <!-- 第三行：手机号 -->
      <van-cell title="手机号" is-link @click="onEditPhone">
        <template #value>
          {{formattedPhoneNumber}}
        </template>
      </van-cell>

      <!-- 第四行：钱包地址 -->
      <van-cell title="钱包地址" class="wallet-cell">
        <template #value>
          {{ formattedWalletAddress }}
        </template>
      </van-cell>
    </van-cell-group>

    <van-action-sheet v-model:show="showActionSheet" :actions="actions" @select="onActionSelect" />
  </div>
</template>

<script setup>
import { ref,computed } from 'vue';
import { showToast } from 'vant';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import { uploadAvatar,getUserInfo } from '@/api/setting';

const store = useStore();
const router = useRouter();
const showActionSheet = ref(false); // 控制 ActionSheet 显示与隐藏
const actions = [
  { name: '选择相册图片上传' },
  { name: '取消', cancel: true }
];
const walletAddress = computed(() => store.getters.getUserInfo?.walletAddress || '');
const phoneNumber = computed(() => store.getters.getUserInfo?.phoneNumber || '');
const username = computed(() => store.getters.getUserInfo?.username || '');
const avatar = computed(() => store.getters.getUserInfo?.avatar || '');

const formattedAvatarUrl = computed(() => {
  return avatar.value ? require('@/'+avatar.value) : require('@/assets/images/sorry.jpg');
});
const formattedWalletAddress = computed(() => {
  if (walletAddress.value) {
    return walletAddress.value.slice(0, 6) + '****' + walletAddress.value.slice(-4);
  }
  return '';
});
const formattedPhoneNumber = computed(() => {
  if (phoneNumber.value) {
    return phoneNumber.value.slice(0, 3) + '****' + phoneNumber.value.slice(-4);
  }
  return '';
}); 

const onAvatarClick = () => {
  showActionSheet.value = true;
};
// 选择 ActionSheet 中的选项
const onActionSelect = (action, index) => {
  if (action.cancel) {
    showActionSheet.value = false;
    return;
  }
  if (index === 0) {
    handleImageUpload();
  }
};

// 处理图片上传
const handleImageUpload = () => {
  // 创建一个 input 元素来选择文件
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = 'image/*'; // 只接受图片文件

  // 监听文件选择
  input.onchange = async (event) => {
    const file = event.target.files[0];
    if (!file) return;
    
    // 验证图片大小和类型
    const MAX_FILE_SIZE = 2 * 1024 * 1024;
    const ALLOWED_TYPES = ['image/jpeg', 'image/png', 'image/gif'];
    
    if (file.size > MAX_FILE_SIZE) {
      showToast('图片大小不能超过2MB');
      return;
    }
    
    if (!ALLOWED_TYPES.includes(file.type)) {
      showToast('请上传JPEG/PNG/GIF格式图片');
      return;
    }
    
    try {
      // 创建FormData对象并上传
      const formData = new FormData();
      formData.append('avatar', file);
      
      // 显示上传中提示
      showToast({ message: '上传中...', duration: 0, forbidClick: true });
      
      // 调用上传API
      console.log('调用 uploadAvatar...');
      const response = await uploadAvatar(formData);
      console.log('uploadAvatar 响应:', response);
      if (response.code !== 200) {
        showToast('头像上传失败');
        return;
      }
      
      // 更新store中的用户信息
      const userInfo = await getUserInfo(); // 这里应该触发请求
      store.commit('setUserInfo', userInfo.data);
      showToast('头像上传成功');
    } catch (error) {
      showToast('头像上传失败');
      console.error('上传错误:', error);
    }
  };
  
  input.click();
  showActionSheet.value = false;
};


const onBack = () => {
  // 返回上一页
  router.go(-1);
};

const onEditNickname = () => {
  // 编辑昵称逻辑
  router.push('/editNickname');
};

const onEditPhone = () => {
  // 编辑手机号逻辑
  router.push('/editPhone');
};
</script>

<style scoped>
/* 页面背景颜色 */
.user-info-page {
  background-color: #f7f8fa;
  /* 浅灰色背景 */
  min-height: 100vh;
  /* 占满整个视口高度 */
  padding: 0;
  margin: 0;
  user-select: none;
  /* 禁止用户选择文本 */
}

/* 容器样式 */
.user-info-container {
  background-color: #ffffff;
  /* 纯白色 */
  border-radius: 16px;
  /* 圆角 */
  margin: 16px;
  /* 外边距 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  /* 添加柔和阴影 */
  overflow: hidden;
  /* 确保圆角生效 */
}

/* 头像样式 */
.avatar {
  width: 26px;
  /* 头像宽度 */
  height: 26px;
  /* 头像高度 */
  border-radius: 50%;
  /* 圆形头像 */
  object-fit: cover;
  /* 确保图片适应容器 */
}
</style>