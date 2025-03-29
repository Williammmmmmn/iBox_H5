<template>
    <div>
      <p>announceId={{ announceId }}</p>
    </div>
  </template>
  
  <script setup>
import { useRoute } from 'vue-router'
    const route = useRoute();
    const announceId = route.params.announceId;
 
  </script>
  
  <style scoped>
  
  </style>