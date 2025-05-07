<template>
    <!-- 页面背景 -->
    <div class="settings-page">
      <!-- 顶部导航 -->
      <van-nav-bar
        title="设置中心"
        left-arrow
        @click-left="onBack"
      />
  
      <!-- 容器 -->
      <van-cell-group class="settings-container">
        <!-- 第一行：个人信息 -->
        <van-cell
          title="个人信息"
          icon="contact"
          is-link
          @click="onPersonalInfo"
          class="custom-cell"
        />
        <!-- 第二行：关于我们 -->
        <van-cell
          title="关于我们"
          icon="info-o"
          is-link
          @click="onAboutUs"
          class="custom-cell"
        />
        <!-- 第三行：退出登录 -->
        <van-cell
          title="退出登录"
          is-link
          @click="onLogout"
          class="custom-cell"
        >
          <template #icon>
            <van-icon name="close" class="custom-icon" />
          </template>
        </van-cell>
      </van-cell-group>
    </div>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router';
  import { showToast } from 'vant';
  
  const router = useRouter();
  
  const onBack = () => {
    // 返回上一页
    router.go(-1);
  };
  
  const onPersonalInfo = () => {
    // 跳转到个人信息页面
    router.push('/userInfo');
  };
  
  const onAboutUs = () => {
    showToast('Hello,iBox');
  };
  
  const onLogout = () => {
    localStorage.removeItem('token');        // 清除 token
    sessionStorage.removeItem('token');      // 清除 session 中的 token（如果有的话）
    localStorage.removeItem('userInfo');     // 清除用户信息
    sessionStorage.removeItem('userInfo');   // 清除 session 中的用户信息（如果有的话）
    localStorage.removeItem('layout-setting'); // 清除布局设置（可选）

    // 跳转到登录页
    router.push('/login');
  };
  </script>
  
  <style scoped>
  /* 页面背景颜色 */
  .settings-page {
    background-color: #f7f8fa; /* 浅灰色背景 */
    min-height: 100vh; /* 占满整个视口高度 */
    padding: 0;
    margin: 0;
    user-select: none;
  }
  /* 调整图标和文字的间距 */
.custom-cell .van-cell__title {
  margin-left: 24px; /* 增加图标与文字的间距 */
}
  /* 容器样式 */
  .settings-container {
    background-color: #ffffff; /* 纯白色 */
    border-radius: 16px; /* 圆角 */
    margin: 16px; /* 外边距 */
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 添加柔和阴影 */
    overflow: hidden; /* 确保圆角生效 */
  }

  /* 自定义单元格样式 */
  .custom-cell {
    padding: 16px 20px; /* 增加单元格内边距 */
    font-size: 16px; /* 调整文字大小 */
    line-height: 24px; /* 调整行高 */
    align-items: center; /* 垂直居中 */
  }
  
  /* 自定义图标样式 */
  .custom-icon {
    font-size: 16px; /* 调整图标大小 */
    vertical-align: middle; /* 垂直居中对齐 */
    color: #ff4d4f; /* 图标颜色 */
  }
  
  /* 调整图标和文字的间距 */
  .van-cell__title {
    margin-left: 16px; /* 增加图标与文字的间距 */
  }
  
  /* 顶部导航样式 */
  .van-nav-bar {
    background-color: #ffffff; /* 纯白色背景 */
    border-bottom: 1px solid #ebebeb; /* 底部边框 */
    width: 100%;
    max-width: 600px; /* 与容器宽度一致 */
  }
  </style>