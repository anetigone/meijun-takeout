<!-- filepath: g:\Documents\GitHub\meijun-takeout\mp-takeout\vue-app\src\views\NotFound.vue -->
<template>
  <div style="text-align:center; margin-top:120px;">
    <el-result
      icon="warning"
      title="404 页面未找到"
      sub-title="您访问的页面不存在或已被删除"
    >
      <template #extra>
        <div style="margin-bottom: 16px; color: #909399;">
          {{ countdown }} 秒后将自动返回首页
        </div>
        <el-button type="primary" @click="goHome">立即返回首页</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const countdown = ref(3)
const goHome = () => {
  router.push('/')
}

onMounted(() => {
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
      goHome()
    }
  }, 1000)
})
</script>