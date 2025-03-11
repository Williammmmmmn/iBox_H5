module.exports = {
  devServer: {
    open: true, // 自动打开浏览器
    proxy: {
      '/api': { // 匹配所有以 /api 开头的请求
        target: 'http://localhost:8086', // 后端地址
        changeOrigin: true, // 允许跨域
        pathRewrite: {
          '^/api': '' // 去掉 /api 前缀
        }
      }
    }
  }
};