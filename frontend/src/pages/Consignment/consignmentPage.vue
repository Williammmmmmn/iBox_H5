<template>
    <div>
      <h1>NFT 寄售页面</h1>
      <p>NFT ID: {{ nftId }}</p>
      <!-- 在这里展示 NFT 的详细信息 -->
    </div>
  </template>
  
  <script setup>
  import { useRoute } from 'vue-router';
  
  const route = useRoute();
  const nftId = route.query.id; // 获取路由参数中的 NFT ID
  </script>