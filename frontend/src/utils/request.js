import axios from 'axios';

// 创建 axios 实例
const request = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // 从环境变量中读取 API 基础地址
  timeout: 5000, // 请求超时时间
});

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 在发送请求之前
    // 添加 token 到请求头
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    // 确保 responseType 被正确传递
    return config;

  },
  (error) => {
    // 对请求错误
    console.error('请求错误111', error);
    return Promise.reject(error);
  }
);


//响应拦截器
request.interceptors.response.use(
  (response) => {
    // 如果是二进制数据（如图片、文件），直接返回 response
    if (response.config.responseType === 'blob') {
      return response;
    }

    // 对 JSON 数据统一处理响应格式
    if (response.data.code === 200) {
      return response.data; // 返回实际数据
    } else {
      return Promise.reject(response.data.message || '请求失败');
    }
  },
  (error) => {
    // 对响应错误
    // 统一处理错误状态码
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // 未授权，跳转到登录页
          window.location.href = '/login';
          break;
        case 404:
          console.error('请求的资源不存在');
          break;
        case 500:
          console.error('服务器内部错误');
          break;
        default:
          console.error('请求失败', error.response.status);
      }
    } else {
      console.error('网络错误，请检查网络连接');
    }
    return Promise.reject(error);
  }
);

// 导出封装的请求方法
export default request;