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
    if (response.config.responseType === 'blob') {
      return response;
    }
    if (response.data.code === 200) {
      return response.data;
    } else {
      // 关键修改：直接抛出后端返回的完整响应数据
      return Promise.reject(response.data);
    }
  },
  (error) => {
    if (error.response) {
      // 关键修改：直接传递 error.response.data
      return Promise.reject(error.response.data);
    }
    console.error('网络错误，请检查网络连接');
    return Promise.reject(new Error('网络错误'));
  }
);

// 导出封装的请求方法
export default request;