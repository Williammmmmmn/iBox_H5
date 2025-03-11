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