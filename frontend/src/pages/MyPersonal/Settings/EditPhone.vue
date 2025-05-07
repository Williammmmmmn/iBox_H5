<template>
  <div class="edit-phone-page">
    <!-- 顶部导航 -->
    <van-nav-bar
      title="换绑手机号"
      left-arrow
      @click-left="onBack"
    />

    <!-- 手机号和验证码输入 -->
    <div class="input-container">
      <!-- 新手机号 -->
      <div class="input-title">新手机号</div>
      <van-field
        v-model="newPhone"
        placeholder="请输入新手机号"
        type="tel"
        maxlength="11"
        clearable
      />

      <!-- 验证码 -->
      <div class="input-title">验证码</div>
      <div class="verification-row">
        <van-field
          v-model="verificationCode"
          placeholder="请输入验证码"
          maxlength="6"
          clearable
        />
        <button
          class="get-code-button"
          :disabled="isCodeSent"
          @click="onGetCode"
        >
          {{ isCodeSent ? `${countdown}s后重试` : "获取验证码" }}
        </button>
      </div>
    </div>

    <!-- 提交按钮 -->
    <div class="submit-button-container">
      <van-button
        type="primary"
        block
        :disabled="!canSubmit"
        @click="onSubmit"
      >
        确认更换
      </van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { showToast } from 'vant';
import { sendChangePhoneCode, changePhone } from '@/api/setting';

const router = useRouter();
const newPhone = ref(''); // 新手机号
const verificationCode = ref(''); // 验证码
const isCodeSent = ref(false); // 是否已发送验证码
const countdown = ref(60); // 倒计时

// 计算属性：是否可以提交
const canSubmit = computed(() => {
  return newPhone.value.trim().length === 11 && 
         verificationCode.value.trim().length === 6;
});

const onBack = () => {
  router.go(-1);
};

const onGetCode = async () => {
  if (!newPhone.value.trim()) {
    showToast('请输入新手机号');
    return;
  }

  if (!/^1[3-9]\d{9}$/.test(newPhone.value)) {
    showToast('手机号格式不正确');
    return;
  }

  try {
    await sendChangePhoneCode(newPhone.value);
    showToast('验证码已发送');
    isCodeSent.value = true;

    // 开始倒计时
    const timer = setInterval(() => {
      countdown.value--;
      if (countdown.value <= 0) {
        clearInterval(timer);
        isCodeSent.value = false;
        countdown.value = 60;
      }
    }, 1000);
  } catch (error) {
    showToast(error.message || '验证码发送失败');
  }
};

const onSubmit = async () => {
  try {
    await changePhone(newPhone.value, verificationCode.value);
    showToast('手机号更换成功');
    // 清除旧token，强制重新登录
    localStorage.removeItem('token');
    router.push('/login');
  } catch (error) {
    showToast(error.message || '手机号更换失败');
  }
};
</script>

<style scoped>
.input-container {
  padding: 16px;
  user-select: none;
}

.input-title {
  margin: 16px 0 8px;
  color: #666;
  font-size: 14px;
}

.verification-row {
  display: flex;
  align-items: center;
  justify-content: space-between; /* 确保输入框和按钮在同一行 */
  white-space: nowrap; /* 防止文字换行 */
}

.get-code-button {
  margin-left: 12px;
  padding: 0 12px;
  height: 44px;
  background: #fff;
  border: 1px solid #eee;
  border-radius: 4px;
  color: #1989fa;
  font-size: 14px;
  white-space: nowrap;
}

.get-code-button:disabled {
  color: #999;
  background: #f5f5f5;
}

.submit-button-container {
  padding: 16px;
  margin-top: 32px;
}
</style>