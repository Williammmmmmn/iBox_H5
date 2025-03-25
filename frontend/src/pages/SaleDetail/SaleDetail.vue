<template>

  <div class="container">
    <div class="scroll-container">
      <!-- 背景图片容器 -->
      <div class="background-image" :style="{ backgroundImage: `url(${require('@/assets/images/a1.jpg')}` }">
        <!-- 新增的模糊层 -->
        <div class="background-blur"></div>
        <!-- 返回箭头 -->
        <div class="header">
          <div class="arrow-container" @click="goBack">
            <van-icon name="arrow-left" class="left-arrow" />
          </div>
        </div>

        <!-- 3D相框容器 -->
        <div class="overlay-image-container">
          <!-- 新增的底座 -->
          <div class="pedestal">
            <div class="pedestal-base"></div>
          </div>
          <!-- 3D相框容器 -->
          <div class="photo-frame">
            <div class="frame-inner">
              <img :src="require('@/assets/images/a1.jpg')" class="overlay-image" />
            </div>
            <!-- 新增的阴影 -->
            <div class="frame-shadow"></div>
          </div>
        </div>
      </div>

      <!-- 修改后的深色背景区域 -->
      <div class="dark-background">
        <!-- 图片名称 -->
        <div class="image-name">神王</div>

        <!-- 发行和流通信息 -->
        <div class="info-container">
          <div class="info-item">
            <span class="label">发行</span>
            <span class="value">5000份</span>
          </div>
          <div class="info-item">
            <span class="label">流通</span>
            <span class="value">5000份</span>
          </div>
        </div>
        <!-- 新增的藏品故事部分 -->
        <div class="story-section">
          <div class="story-title">
            <span class="line"></span>
            <span class="text">藏品故事</span>
            <span class="line"></span>
          </div>
          <div class="story-content">
            这件名为"神王"的藏品源自古老东方文明，传说中是上古时期神王佩戴的圣物。历经千年传承，每一处纹路都记载着不同时代的传奇故事。如今以数字艺术形式重现，将传统文化与现代科技完美融合，成为连接古今的艺术珍品。
          </div>
        </div>
      </div>

      <!-- 底部白色区域 -->
      <div class="bottom-sheet-container">
        <div class="bottom-sheet">
          <div class="white-top"></div> <!-- 新增的顶部圆角装饰 -->
          <van-tabs v-model="activeTab" class="tabs">
            <van-tab title="购买">
              <div class="content">
                <div class="price-section">
                  <div class="left">
                    <div class="price">￥20000</div>
                    <div class="market-list" @click="goBack">
                      市场列表
                      <van-icon name="arrow" class="chevron" />
                    </div>
                  </div>
                  <van-button type="primary" class="buy-button">立即购买</van-button>
                </div>
              </div>
            </van-tab>
            <van-tab title="相关公告">
              <div class="content">
                <!-- 公告内容 -->
              </div>
            </van-tab>
          </van-tabs>
        </div>
      </div>

    </div>

  </div>
</template>

<script>
export default {
  name: 'MySaleDetail',
  data() {
    return {
      activeTab: 0
    }
  }
}
</script>

<script setup>
import { useRouter } from 'vue-router';
// import { ref } from 'vue';
const router = useRouter();

// const route = useRoute();
// const nftId = route.params.nftId;
// const instanceId = route.params.instanceId;

const goBack = () => {
  router.go(-1);
};
</script>

<style scoped>
.container {
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.7);
}

.scroll-container {
  width: 100%;
  height: 100%;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

/* 固定顶部图片区域 */
.fixed-image-area {
  height: 60vh;
  position: relative;
  flex-shrink: 0;
}

.background-image {
  position: relative;
  width: 100%;
  height: 60vh;
  background-size: cover;
  background-position: center;
  z-index: 1;
}

/* 添加深色叠加层 */
.background-image::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.9);
  /* 半透明黑色叠加 */
  z-index: 1;
}

.background-blur {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: inherit;
  background-size: cover;
  background-position: center;
  filter: blur(4px);
  z-index: 2;
  pointer-events: none;
}

.background-image::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 20%;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
}

.dark-background {
  padding: 15px;
  flex-shrink: 0;
  padding: 15px;
  padding-top: 30px;
  /* 给过渡区域留出空间 */
  margin-top: -10px;
  /* 向上偏移覆盖过渡区域 */
  position: relative;
  z-index: 2;
}

.header {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  padding: 10px;
  z-index: 10;
}

.arrow-container {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  cursor: pointer;
}

.left-arrow {
  font-size: 20px;
  color: white;
}

.overlay-image-container {
  position: absolute;
  bottom: 20%;
  left: 45%;
  transform: translateX(-50%);
  width: 66%;
  z-index: 10;
  text-align: center;
  perspective: 1500px;
}
.pedestal {
  position: absolute;
  width: 100%;
  height: 100px;
  bottom: -60px;
  left: 45%;
  transform: translateX(-50%);
  z-index: 5;
  perspective: 1000px;
  filter: drop-shadow(0 10px 15px rgba(0, 0, 0, 0.4));
}



/* 底座主体 - 深色带金边 */
.pedestal-base {
  position: absolute;
  width: 112%;
  height: 60px;
  bottom: 0;
  left: 5%;
  background: 
    linear-gradient(
      to bottom, 
      rgba(213, 219, 139, 0.95) 0%, 
      rgba(86, 84, 67, 0.95) 100%
    );
  border-radius: 3px;
  box-shadow: 
    0 8px 20px rgba(0, 0, 0, 0.5),
    inset 0 0 15px rgba(212, 175, 55, 0.4);
  transform: perspective(500px) rotateX(20deg);
  border: 1px solid rgba(212, 175, 55, 0.6);
}

/* 底部金色装饰线 */
.pedestal::after {
  content: '';
  position: absolute;
  width: 112%;
  height: 3px;
  bottom: 8px;
  left: 4%;
  background: linear-gradient(
    to right,
    transparent 0%,
    rgba(212, 175, 55, 0.7) 20%,
    rgba(212, 175, 55, 0.9) 50%,
    rgba(212, 175, 55, 0.7) 80%,
    transparent 100%
  );
  border-radius: 2px;
  box-shadow: 0 0 12px rgba(212, 175, 55, 0.5);
  transform: perspective(500px) rotateX(20deg);
  z-index: 6;
}

/* 侧面金色装饰线 */
.pedestal-base::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  border: 1px solid transparent;
  border-image: linear-gradient(
    to bottom,
    transparent 0%,
    rgba(212, 175, 55, 0.6) 10%,
    transparent 20%,
    transparent 80%,
    rgba(212, 175, 55, 0.4) 90%,
    transparent 100%
  );
  border-image-slice: 1;
  border-radius: 3px;
}
/* 3D相框 */
.photo-frame {
  position: relative;
  width: 100%;
  padding: 5%;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 16px;
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow:
    0 0 20px rgba(0, 0, 0, 0.2),
    0 0 0 1px rgba(255, 255, 255, 0.2);
  transform-style: preserve-3d;
  animation: rotateFrame 8s ease-in-out infinite;
  z-index: 10;
}

.frame-inner {
  position: relative;
  padding: 4px;
  background: linear-gradient(135deg,
      #e6e6e6 0%,
      #ffffff 25%,
      #e6e6e6 50%,
      #ffffff 75%,
      #e6e6e6 100%);
  border-radius: 10px;
  box-shadow:
    inset 0 0 10px rgba(0, 0, 0, 0.2),
    0 2px 10px rgba(0, 0, 0, 0.3);
  transform: translateZ(15px);
}

/* 新增阴影效果 */
.frame-shadow {
  position: absolute;
  width: 90%;
  height: 20px;
  bottom: -30px;
  left: 5%;
  background: radial-gradient(ellipse at center, rgba(0,0,0,0.6) 0%, rgba(0,0,0,0) 70%);
  border-radius: 50%;
  filter: blur(5px);
  transform: rotateX(75deg) scale(0.9);
  opacity: 0.7;
  z-index: -1;
  animation: shadowRotate 8s ease-in-out infinite;
}
@keyframes shadowRotate {
  0%, 100% {
    transform: rotateX(75deg) scale(0.9) translateX(0);
  }
  25% {
    transform: rotateX(75deg) scale(0.9) translateX(10px);
  }
  50% {
    transform: rotateX(75deg) scale(0.9) translateX(0);
  }
  75% {
    transform: rotateX(75deg) scale(0.9) translateX(-10px);
  }
}
.overlay-image {
  width: 100%;
  height: auto;
  display: block;
  border-radius: 8px;
  border: 4px solid transparent;
  background:
    linear-gradient(white, white) padding-box,
    linear-gradient(135deg,
      #d1d1d1 0%,
      #ffffff 20%,
      #d1d1d1 40%,
      #ffffff 60%,
      #d1d1d1 80%,
      #ffffff 100%) border-box;
  box-shadow:
    inset 0 0 15px rgba(0, 0, 0, 0.2),
    0 0 10px rgba(255, 255, 255, 0.8);
  background-size: 200% 200%;
  animation: metalShine 3s linear infinite;
}

/* 文字颜色修改 */
.image-name {
  display: flex;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
  margin-top: 0;
  color: white;
  /* 改为白色 */
}

.info-container {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
}

.info-item {
  display: flex;
  align-items: center;
}

.label {
  background-color: rgb(236, 220, 73);
  padding: 2px 3px;
  border-radius: 1px;
  font-size: 10px;
  color: #333;
}

.value {
  background-color: rgba(255, 255, 255, 0.3);
  /* 半透明白色 */
  padding: 2px 3px;
  border-radius: 4px;
  font-size: 10px;
  color: white;
  /* 改为白色 */
  margin-left: 4px;
}

/* 藏品故事样式 */
.story-section {
  margin-top: 20px;
  padding: 0 15px;
}

.story-title {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 15px;
}

.story-title .line {
  width: 20%;
  height: 1px;
  background: rgba(255, 255, 255, 0.3);
}

.story-title .text {
  padding: 0 8px;
  font-size: 20px;
  font-weight: bold;
  color: white;
}

.story-content p {
  font-size: 14px;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.8);
  text-align: justify;
  /* 两端对齐 */
  text-justify: inter-ideograph;
  /* 中文两端对齐优化 */
  hyphens: auto;
  margin: 0;
  padding: 0 5px;
}

.story-content {
  font-size: 14px;
  line-height: 1.6;
  color: rgba(255, 255, 255, 0.8);
  text-align: center;
  padding: 0 10px;
}

/* 新增底部容器 */

/* 底部白色区域样式 */
.bottom-sheet {
  position: relative;
  background: white;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
  padding: 16px;
  padding-top: 2px;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  /* margin-top: auto;  */
  flex-shrink: 0;
}

.white-top {
  position: absolute;
  top: -1%;
  left: 50%;
  transform: translateX(-50%);
  width: 50px;
  height: 4px;
  background: rgb(61, 235, 168);
  border-radius: 2px;
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

.tabs {
  margin-bottom: 10px;
}

.price-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  /* 改为居中对齐 */
  align-items: flex-end;
  margin-top: 18px;
  height: 36px;
  /* 与按钮同高 */
}

.left {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
  /* 继承父级高度 */
}

.price {
  font-size: 19px;
  font-weight: bold;
  color: #161616;
  line-height: 1.2;
  /* 调整行高 */
}

.market-list {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  line-height: 1.2;
  /* 调整行高 */
}

.chevron {
  margin-left: 4px;
}

.buy-button {
  width: 208px;
  height: 36px;
  border-radius: 22px;
  font-weight: bold;
  flex-shrink: 0;
  /* 防止按钮被压缩 */
}

/* 动画保持不变 */
@keyframes metalShine {
  0% {
    background-position: 0% 0%;
  }

  100% {
    background-position: 200% 200%;
  }
}

@keyframes rotateFrame {

  0%,
  100% {
    transform: rotateY(0deg) rotateX(5deg);
  }

  25% {
    transform: rotateY(-20deg) rotateX(5deg);
  }

  50% {
    transform: rotateY(0deg) rotateX(-5deg);
  }

  75% {
    transform: rotateY(20deg) rotateX(5deg);
  }
}
</style>