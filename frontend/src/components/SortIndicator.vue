<!-- 排序组件 -->
<template>
    <div class="sort-indicator" @click="toggleSort">
      <span class="text">{{ label }}</span>
      <div class="triangle-container">
        <span class="triangle triangle-up" :class="{ active: isAsc }"></span>
        <span class="triangle triangle-down" :class="{ active: isDesc }"></span>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, watch,defineProps, defineEmits  } from 'vue';
  const props = defineProps({
    label: {
      type: String,
      required: true
    },
    sortKey: {
      type: String,
      required: true
    },
    currentSort: {
      type: Object,
      default: () => ({ key: '', order: '' })
    }
  });
  
  const emit = defineEmits(['sort-change']);
  
  const isAsc = ref(false);
  const isDesc = ref(false);
  
  const toggleSort = () => {
    let newOrder = '';
    
    if (props.currentSort.key !== props.sortKey) {
      // 第一次点击，默认降序
      newOrder = 'desc';
    } else {
      // 切换顺序
      newOrder = props.currentSort.order === 'asc' ? 'desc' : 'asc';
    }
    
    emit('sort-change', {
      key: props.sortKey,
      order: newOrder
    });
  };
  
  // 监听外部排序状态变化
  watch(() => props.currentSort, (newVal) => {
    if (newVal.key === props.sortKey) {
      isAsc.value = newVal.order === 'asc';
      isDesc.value = newVal.order === 'desc';
    } else {
      isAsc.value = false;
      isDesc.value = false;
    }
  }, { immediate: true });
  </script>
  
<style scoped>
  .sort-indicator {
    display: flex;
    align-items: center;
    cursor: pointer;
    user-select: none;
  }
  
  .text {
    font-size: 10px;
    color: #444343;
  }
  
  .triangle-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-left: 3px;
  }
  
  .triangle {
    display: inline-block;
    width: 0;
    height: 0;
    border-left: 3px solid transparent;
    border-right: 3px solid transparent;
    margin: 1px;
  }
  
  .triangle-up {
    border-bottom: 4px solid #817f7f;
  }
  
  .triangle-down {
    border-top: 4px solid #817f7f;
  }
  
  .triangle-up.active {
    border-bottom: 4px solid blue;
  }
  
  .triangle-down.active {
    border-top: 4px solid blue;
  }
  </style>