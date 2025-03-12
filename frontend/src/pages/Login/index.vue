<template>
    <van-nav-bar left-arrow @click-left="goBack" :border="false" />
    <div class="login-container">
        <div class="login-box">
            <div class="title">
                <h2>欢迎注册/登录iBox</h2>
                <p>未注册号码将自动创建账号</p>
            </div>
            <van-popup v-model:show="showCaptchaDialog" round position="center" :style="{ width: '80%', padding: '20px' }">
                <div class="captcha-container">
                    <span style="margin-bottom: 30px;color: gray;">请完成安全验证</span>
                    <div class="captcha-input-group">
                        <input type="text" v-model="captchaInput" placeholder="请输入验证码" class="captcha-input" />
                        <img :src="captchaImage" @click="refreshCaptcha" alt="验证码" class="captcha-image" />

                    </div>
                    <div class="button-container">
                        <van-button type="primary" @click="handleCaptchaConfirm">确认</van-button>
                        <van-button type="default" @click="showCaptchaDialog = false">取消</van-button>
                    </div>
                </div>
            </van-popup>
            <form @submit.prevent="handleLogin">
                <div class="form-group">
                    <input type="text" id="phone" maxlength="11" v-model="phone" placeholder="请输入本人实名认证的手机号" required />
                </div>
                <div class="form-group">
                    <div class="verification-code-group">
                        <div class="input-with-button" :class="{ 'input-focused': isVerificationCodeFocused }">
                            <input type="text" id="verification-code" maxlength="6" v-model="verificationCode"
                                placeholder="验证码" required @focus="isVerificationCodeFocused = true"
                                @blur="isVerificationCodeFocused = false" />
                            <span v-if="!isCodeSent" class="get-code-text" @click="handleGetVerificationCode">获取验证码</span>
                            <span v-else class="get-code-text disabled">{{ countdown }}秒后重新获取</span>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <input type="text" id="invitation-code" v-model="invitationCode" placeholder="邀请码(选填)" />
                </div>
                <button type="submit" class="login-btn" :class="{ 'active': isLoginButtonActive }"
                    :disabled="!isLoginButtonActive">登录</button>

                <div class="form-group">
                    <label>
                        <input type="checkbox" v-model="agreeTerms" />
                        我已阅读并同意《用户协议》、《隐私政策》
                    </label>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
export default {
    name: 'MyLogin'
}
</script>

<script setup>
import { ref, computed } from 'vue';
import { showConfirmDialog, showToast } from 'vant';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import { login,sendCode,fetchCaptcha,validateCaptcha } from '@/api/user';
const router = useRouter(); // 获取路由实例
const store = useStore(); // 获取 Vuex Store 实例

const phone = ref('');
const verificationCode = ref('');
const isCodeSent = ref(false); // 是否已发送验证码
const invitationCode = ref('');
const agreeTerms = ref(false);
const isVerificationCodeFocused = ref(false); // 验证码输入框是否获得焦点
const countdown = ref(0); // 倒计时秒数
let timer = null; // 定时器

const showCaptchaDialog = ref(false);
const captchaImage = ref('');
const captchaInput = ref('');
const captchaKey = ref(''); // 验证码标识

// 手机号格式验证
const isPhoneValid = computed(() => {
    const phoneRegex = /^1[3456789]\d{9}$/; // 简单的手机号正则表达式
    return phoneRegex.test(phone.value);
});

// 验证码格式验证（假设验证码为6位数字）
const isVerificationCodeValid = computed(() => {
    const codeRegex = /^\d{6}$/; // 6位数字
    return codeRegex.test(verificationCode.value);
});

// 计算属性：判断登录按钮是否激活
const isLoginButtonActive = computed(() => {
    return isPhoneValid.value && isVerificationCodeValid.value;
});
// 发送图片验证码
const refreshCaptcha = async () => {
  try {
    const { captchaKey: key, imageUrl } = await fetchCaptcha(); // 调用 API 获取验证码图片
    if (captchaImage.value) {
      URL.revokeObjectURL(captchaImage.value); // 释放旧的 Blob URL
    }

    captchaImage.value = imageUrl; // 更新验证码图片
    captchaKey.value = key; // 保存验证码标识
  } catch (error) {
    showToast('获取图片验证码失败，请重试');
  }
};

// 处理验证码确认
const handleCaptchaConfirm = async () => {
  if (!captchaInput.value) {
    showToast('请输入验证码');
    return;
  }
  try {
    const response = await validateCaptcha(captchaKey.value, captchaInput.value); // 调用 API 验证图片验证码
    if (response.code === 200) {
      showCaptchaDialog.value = false; // 关闭验证码弹窗
      await sendVerificationCode(); // 验证通过后发送短信验证码
    } else {
      showToast('验证码错误，请重试');
      refreshCaptcha(); // 刷新图片验证码
    }
  } catch (error) {
    showToast('验证码验证失败，请重试');
  }
};

// 发送短信验证码
const sendVerificationCode = async () => {
  if (!isPhoneValid.value) {
    showToast('请输入有效的手机号');
    return;
  }

  try {
    const response = await sendCode(phone.value); // 调用 API 发送短信验证码
    if (response.code === 200) {
      showToast('验证码已发送');
      isCodeSent.value = true;
      countdown.value = 60;

      // 启动倒计时
      timer = setInterval(() => {
        if (countdown.value > 0) {
          countdown.value--;
        } else {
          clearInterval(timer);
          isCodeSent.value = false;
        }
      }, 1000);
    } else {
      showToast(response.data.message || '验证码发送失败');
    }
  } catch (error) {
    showToast('验证码发送失败，请重试');
  }
};

// 点击“获取验证码”按钮
const handleGetVerificationCode = () => {
  if (!isPhoneValid.value) {
    showToast('请输入有效的手机号');
    return;
  }

  showCaptchaDialog.value = true; // 弹出图片验证码弹窗
  refreshCaptcha(); // 加载图片验证码
};

// 登录逻辑
const handleLogin = async () => {
    if (!agreeTerms.value) {
        // 如果未同意协议，弹出 Dialog
        showConfirmDialog({
            title: '提示',
            message: '请您仔细阅读《用户协议》、《隐私政策》，点击“确定”，表示您已经阅读并同意以上协议。',
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            confirmButtonColor: '#007bff', // 确定按钮颜色为蓝色
            cancelButtonColor: '#007bff', // 取消按钮颜色为蓝色
        })
            .then(() => {
                // 用户点击确定
                agreeTerms.value = true; // 标记为已同意
                handleLogin(); // 重新尝试登录
            })
            .catch(() => {
                // 用户点击取消
                console.log('用户取消');
            });
    } else {
        // 登录逻辑
        try {
            // 调用登录 API
            const response = await login(phone.value, verificationCode.value, invitationCode.value);
            // 处理登录成功的情况
            if (response.code === 200) {
                // 保存用户信息到本地存储或 Vuex
                store.dispatch('login', {
                    token: response.data.token,
                    userInfo: response.data.user
                })

                // 跳转到首页或其他受保护的页面
                router.push('/home');
                // 弹出登录成功提示
                showToast('登录成功');
            }
            else {
                // 处理登录失败的情况
                showToast(response.message || '登录失败，请重试'); // 其他错误提示
            }
        } catch (error) {
            // 处理网络错误或其他异常
            showToast(error);
        }
    }
};

const goBack = () => {
    router.push('/home'); // 跳转到首页
};
</script>

<style scoped>
.login-container {
    user-select: none;
    margin-top: 10px;
    display: flex;
    height: 100vh;
}

.title {
    padding-left: 0px;
    margin-bottom: 20px;
    pointer-events: none;
}

.title h2 {
    font-weight: 200;
    margin: 0;
    font-size: 24px;

}

.title p {
    margin: 0px 0 0 0;
    color: #a3a3a3;
    font-size: 13px;

}

.login-box {
    background-color: #fff;
    padding: 20px;
    border-radius: 8px;
    width: 300px;
}

.form-group {
    margin-bottom: 15px;
}

.login-container input[type="text"]:nth-child(1),
.login-container input[type="text"]:nth-child(3) {
    width: 100%;
    padding: 8px 0;
    border: none;
    border-bottom: 1px solid #eeeded;
    outline: none;
    font-size: 16px;
    background-color: transparent;
}

.login-container input[type="text"]:not(:nth-child(2)) {
    width: 100%;
    padding: 8px 0;
    border: none;
    border-bottom: 1px solid #eeeded;
    outline: none;
    font-size: 16px;
    background-color: transparent;
}



.input-with-button {
    display: flex;
    align-items: center;
    position: relative;
    /* 为伪元素定位 */
}

.input-with-button::after {
    content: '';
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
    height: 1px;
    background-color: #eeeded;
    /* 灰色线条 */
}



.get-code-text {
    color: #007bff;
    font-size: 14px;
    cursor: pointer;
    margin-left: 10px;
    white-space: nowrap;
}

.get-code-text.disabled {
    color: #ccc;
    cursor: not-allowed;
}

.verification-code-group {
    display: flex;
}


label {
    display: flex;
    align-items: center;
    /* 垂直居中 */
    font-size: 12px;
    color: #666;
    cursor: pointer;
}

input[type="checkbox"] {
    margin-right: 8px;
    /* 复选框与文本之间的间距 */
    appearance: none;
    /* 移除默认样式 */
    width: 16px;
    /* 设置宽度 */
    height: 16px;
    /* 设置高度 */
    border: 2px solid #ccc;
    /* 添加边框 */
    border-radius: 50%;
    /* 设置为圆形 */
    cursor: pointer;
    position: relative;
}

/* 选中状态 */
input[type="checkbox"]:checked {
    background-color: #007bff;
    /* 选中时的背景颜色 */
    border-color: #007bff;
    /* 选中时的边框颜色 */
}

/* 选中时的内部小圆点 */
input[type="checkbox"]:checked::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 8px;
    /* 小圆点的宽度 */
    height: 8px;
    /* 小圆点的高度 */
    background-color: white;
    /* 小圆点的颜色 */
    border-radius: 50%;
    /* 小圆点为圆形 */
    transform: translate(-50%, -50%);
    /* 居中 */
}

.login-btn {
    width: 100%;
    padding: 10px;
    background-color: #ababab;
    /* 默认灰色 */
    color: #fff;
    border: none;
    border-radius: 16px;
    cursor: pointer;
    transition: background-color 0.3s ease;
    /* 添加过渡效果 */
}

.login-btn.active {
    background-color: #007bff;
    /* 激活状态为绿色 */
}

.captcha-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 0px;
    border-radius: 12px; /* 添加圆角 */
    background-color: #ffffff; /* 背景颜色 */
}

.captcha-image {
    width: 90px;
    height: 32px;
    cursor: pointer;
    margin-right: 8px;
}
.captcha-input-group {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
}
.captcha-input {
    flex:1;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 16px;
}

.button-container {
    display: flex;
    justify-content: space-between;
    width: 100%;
    align-items: center; /* 垂直居中 */
    margin-top: 10px; /* 添加一些间距 */
}

/* 为按钮设置统一的样式 */
.button-container .van-button {
    height: 36px; /* 减小按钮高度 */
    line-height: 36px; /* 调整文字垂直居中 */
    padding: 0 12px; /* 调整按钮的水平内边距 */
    font-size: 14px; /* 调整字体大小 */
}
</style>