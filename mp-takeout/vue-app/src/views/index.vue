<template>
  <el-container style="height: 100vh; width: 100%;">
    <!-- 主体内容 -->
    <el-main style="padding: 0; display: flex; align-items: center; justify-content: center;">
      <div class="hero-section">
        <h1 style="font-size: 3em; margin-bottom: 20px; color: #333;">{{  line1Display }}</h1>
        <p style="font-size: 1.2em; color: #666; margin-bottom: 30px;">{{ line2Display }}</p>
        <el-button type="primary" size="large" @click="$router.push('/login')">立即开始</el-button>
      </div>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const line1 = '欢迎使用 MeiJun 在线点餐系统'
const line2 = '一个简洁高效的在线点餐平台'

const line1Display = ref('')
const line2Display = ref('')

function typeWriter(text: string, displayRef: any, delay = 80, cb?: () => void) {
  let i = 0
  function typing() {
    if (i < text.length) {
      displayRef.value += text[i]
      i++
      setTimeout(typing, delay)
    } else if (cb) {
      cb()
    }
  }
  typing()
}

typeWriter(line1, line1Display, 80, () => {
  setTimeout(() => {
    typeWriter(line2, line2Display, 80)
  }, 400)
})
</script>

<style scoped>
.primary-header {
  height: 60px;
}

.secondary-header {
  height: 40px;
}

.hero-section h1 {
  background: linear-gradient(90deg, #409EFF 20%, #66b1ff 80%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  color: transparent;
}

.hero-section {
  text-align: center;
  padding: 40px;
  background: linear-gradient(to right, #eaf4ff, #f7fcff);
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  max-width: 800px;
  width: 100%;
  animation: gradientAnimation 5s linear infinite;
}

@keyframes gradientAnimation {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

.el-main {
  background-image: url('../assets/image/bg.jpg');
  background-size: cover;
  background-position: center;
}

</style>
