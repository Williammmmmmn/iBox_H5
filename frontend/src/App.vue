<template>
  <router-view v-slot="{ Component }">
    <keep-alive>
      <component 
        :is="Component" 
        v-if="$route.meta.keepAlive"
        :key="$route.fullPath" 
      />
    </keep-alive>
    <component 
      :is="Component" 
      v-if="!$route.meta.keepAlive"
      :key="$route.fullPath"
    />
  </router-view>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {};
  },
};
</script>

<style scoped>
/* 全局样式 */
</style>