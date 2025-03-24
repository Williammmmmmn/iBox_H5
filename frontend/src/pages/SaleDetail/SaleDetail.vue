<template>
    <div>
        <p>寄售详情页面</p>
        <p>nftId: {{ nftId }}</p>
        <p>instanceId: {{ instanceId }}</p>
    </div>
</template>
<script>
export default {
    name: 'MySaleDetail'
}
</script>
<script setup>
import { useRoute } from 'vue-router';
const route = useRoute();
const nftId = route.params.nftId;
const instanceId = route.params.instanceId;

</script>

<style scoped></style>