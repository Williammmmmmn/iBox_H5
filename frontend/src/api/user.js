import request from '@/utils/request';

//登录方法
export function login(phone, verificationCode, invitationCode) {
    return request({
        url: '/user/login',
        method: 'post',
        data: {
            phone,
            verificationCode,
            invitationCode
        }
    }).then(response => {
        // 返回完整的响应对象
        return response;
    })
}
//发送验证码
export function sendCode(phone) {
    return request({
        url: '/auth/sendCode',
        method: 'post',
        params: { phone }
    }).then(response => {
        // 返回完整的响应对象
        return response;
    })
}

export function fetchCaptcha() {
    return request({
        url: '/captcha',
        method: 'get',
        responseType: 'blob', // 确保正确处理图片流
    })
        .then(response => {
            const captchaKey = response.headers['captcha-key']; // 从响应头中获取验证码标识
            const imageUrl = URL.createObjectURL(response.data); // 将图片流转换为 URL
            return { captchaKey, imageUrl }; // 返回验证码标识和图片 URL
        })
        .catch(error => {
            console.error('response:', error);
        });
}

// 验证图片验证码
export function validateCaptcha(captchaKey, captcha) {
    return request({
        url: '/validate-captcha',
        method: 'post',
        data: { captchaKey, captcha }
    });
}