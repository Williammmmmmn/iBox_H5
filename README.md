# IBox交易平台
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
一个基于Vue3前端和Spring Boot后端的藏品交易平台。
## 1.项目介绍
### 1.1功能特性
- 用户注册与认证登录
- 藏品上架与展示
- 交易市场浏览
- 购买与出售功能
- 求购功能
- 公告功能
- 个人藏品展示
- 我的订单管理
- 我的钱包管理
- 个人设置管理

### 1.2 技术栈

####  1.2.1前端技术栈
##### 1.2.1.1  **核心框架**  
[![Vue](https://img.shields.io/badge/Vue-3.2.13-%2342b883)](https://vuejs.org/)
[![Vue Router](https://img.shields.io/badge/Vue_Router-4.5.0-%2342b883)](https://router.vuejs.org/)
[![Vuex](https://img.shields.io/badge/Vuex-4.0.2-%2342b883)](https://vuex.vuejs.org/)
##### 1.2.1.1  **UI组件库**  
[![Vant](https://img.shields.io/badge/Vant-4.9.17-%23ff4d4f)](https://vant-ui.github.io/vant/#/zh-CN)
##### 1.2.1.1  **HTTP客户端**  
[![Axios](https://img.shields.io/badge/Axios-1.7.9-%235a29e4)](https://axios-http.com/)
####  1.2.2后端技术栈
##### 1.2.1.1  **核心框架**  
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.6.13-%236DB33F)](https://spring.io/projects/spring-boot)
[![MyBatis](https://img.shields.io/badge/MyBatis-2.3.2-%23e7572b)](https://mybatis.org/mybatis-3/)

##### 1.2.1.1  **安全认证**  
[![Spring Security](https://img.shields.io/badge/Spring_Security-5.6.7-%236DB33F)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-0.11.2-%23000000)](https://jwt.io/)

##### 1.2.1.1  **数据库**  
[![MySQL](https://img.shields.io/badge/MySQL-8.0-%234479A1)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.2-%23DC382D)](https://redis.io/)

##### 1.2.1.1  **中间件**  
[![Redisson](https://img.shields.io/badge/Redisson-3.17.6-%23e60000)](https://redisson.org/)
[![Quartz](https://img.shields.io/badge/Quartz-2.3.2-%23ff6600)](http://www.quartz-scheduler.org/)

##### 1.2.1.1  **工具**  
[![Lombok](https://img.shields.io/badge/Lombok-1.18.24-%23000000)](https://projectlombok.org/)
[![PageHelper](https://img.shields.io/badge/PageHelper-1.4.6-%2300B4FF)](https://pagehelper.github.io/)

### 1.3 开发工具
**前端开发工具**
   Visual Studio Code
   
 **后端开发工具**
   IntelliJ IDEA

## 2.快速开始

### 2.1前置要求

- Node.js (版本16.13.2)
- Java JDK (版本1.8)
- MySQL (版本5.6.10)
- Redis(版本5.0.14)

### 2.2安装与运行

#### 2.2.1**后端服务**

1. 创建数据库并配置`application.yml`
	配置本地**datasource**
	**redis**默认密码为空
	配置**file:  upload-dir**上传路径为本机文件夹
2. 启动后端服务：IBoxBackEndApplication
#### 2.2.1**前端服务**
1. 安装依赖 
	**npm install**
2. 启动开发服务器 
	**npm run dev**

## 3.效果展示
	
<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px;">
  <img src="./introduceImages/注册登录.png" width="200" alt="注册登录">
  <img src="./introduceImages/首页.png" width="200" alt="首页">
  <img src="./introduceImages/我的.png" width="200" alt="我的">
</div>

</br>
</br>
</br>

<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px;">
  <img src="./introduceImages/市场.png" width="200" alt="市场">
  <img src="./introduceImages/首页搜索.png" width="200" alt="首页搜索">
  <img src="./introduceImages/价格列表.png" width="200" alt="价格列表">	
</div>

</br>
</br>
</br>

<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px;">
  <img src="./introduceImages/求购列表.png" width="200" alt="求购列表">
  <img src="./introduceImages/藏品展示.png" width="200" alt="藏品展示">
  <img src="./introduceImages/确认订单.png" width="200" alt="确认订单">
</div>

</br>
</br>
</br>

<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px;">
  <img src="./introduceImages/公告列表.png" width="200" alt="公告列表">
  <img src="./introduceImages/公告详情.png" width="200" alt="公告详情">
  <img src="./introduceImages/确认订单.png" width="200" alt="发起求购">
</div>


</br>
</br>
</br>

<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px;">
  <img src="./introduceImages/求购出售.png" width="200" alt="求购出售">
  <img src="./introduceImages/资产藏品弹框.png" width="200" alt="资产藏品弹框">
  <img src="./introduceImages/钱包.png" width="200" alt="钱包">
</div>

## 4.










