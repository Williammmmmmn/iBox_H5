<template>
  <van-tabbar route active-color="blue" inactive-color="#000" style="user-select: none">
    <van-tabbar-item icon="home-o" to="/home">首页</van-tabbar-item>
    <van-tabbar-item icon="friends-o" to="/community">社区</van-tabbar-item>
    <van-tabbar-item icon="user-o" to="/user">我的</van-tabbar-item>
  </van-tabbar>
  <router-view/>
</template>

<script>
export default {
  name: 'MyIndex',
}
</script>

<style>

</style>