<template>
  <div class="game-container">
    <!-- 游戏信息栏 -->
    <div class="game-header">
      <span>得分: {{ score }}</span>
      <span>剩余时间: {{ timeLeft }} 秒</span>
      <div>
        <el-button @click="togglePause" type="info">{{ isPaused ? '继续' : '暂停' }}</el-button>
        <el-button @click="restartGame" type="primary">重新开始</el-button>
      </div>
    </div>

    <!-- 游戏区域 -->
    <div class="game-area" ref="gameArea">
      <div
        v-for="(balloon, index) in balloons"
        :key="index"
        class="balloon"
        :style="{
          top: balloon.top + 'px',
          left: balloon.left + 'px',
          backgroundColor: balloon.color,
          width: balloon.size + 'px',
          height: balloon.size + 'px'
        }"
        @click="popBalloon(index)"
      >
        💥
      </div>
    </div>

    <!-- 游戏状态提示 -->
    <div v-if="isPaused && !showGameOver" class="game-overlay">
      <h3>游戏已暂停</h3>
    </div>
    <div v-if="showGameOver" class="game-overlay">
      <h3>游戏结束</h3>
      <p>你的得分是：{{ score }}</p>
      <el-button type="primary" @click="restartGame">再玩一次</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'

// 数据定义
const gameArea = ref<HTMLElement | null>(null)
const score = ref<number>(0)
const timeLeft = ref<number>(60)
const balloons = ref<Array<{ top: number; left: number; color: string; size: number }>>([])
const showGameOver = ref<boolean>(false)
const isPaused = ref<boolean>(false)

let timer: number | null = null
let balloonInterval: number | null = null

// 生成气球
function createBalloon() {
  if (!gameArea.value || isPaused.value) return
  const areaWidth = gameArea.value.clientWidth
  const areaHeight = gameArea.value.clientHeight
  const size = Math.floor(Math.random() * 40) + 30 // 30~70px
  const top = Math.random() * (areaHeight - size)
  const left = Math.random() * (areaWidth - size)
  const colors = ['#FF5E5B', '#FFD166', '#06D6A0', '#118AB2', '#EF476F']
  const color = colors[Math.floor(Math.random() * colors.length)]

  balloons.value.push({ top, left, color, size })
}

// 点击气球
function popBalloon(index: number) {
  if (isPaused.value) return
  balloons.value.splice(index, 1)
  score.value += 1
}

// 开始游戏
function startGame() {
  score.value = 0
  timeLeft.value = 60
  balloons.value = []
  showGameOver.value = false
  isPaused.value = false

  // 倒计时
  timer = window.setInterval(() => {
    timeLeft.value--
    if (timeLeft.value <= 0) {
      endGame()
    }
  }, 1000)

  // 定时生成气球
  balloonInterval = window.setInterval(createBalloon, 800)
}

// 暂停/继续游戏
function togglePause() {
  isPaused.value = !isPaused.value
}

// 重新开始
function restartGame() {
  if (timer !== null) clearInterval(timer)
  if (balloonInterval !== null) clearInterval(balloonInterval)
  startGame()
}

// 结束游戏
function endGame() {
  if (timer !== null) clearInterval(timer)
  if (balloonInterval !== null) clearInterval(balloonInterval)
  showGameOver.value = true
}

// 页面加载自动开始
onMounted(() => {
  startGame()
})

// 页面卸载时清除定时器
onBeforeUnmount(() => {
  if (timer !== null) clearInterval(timer)
  if (balloonInterval !== null) clearInterval(balloonInterval)
})
</script>

<style scoped>
.game-container {
  position: relative;
  min-height: 100vh;
  background: linear-gradient(to bottom right, #a8edea, #fed6e3);
  font-family: Arial, sans-serif;
}

.game-header {
  display: flex;
  justify-content: space-between;
  padding: 16px 32px;
  background-color: rgba(255, 255, 255, 0.8);
  font-weight: bold;
  font-size: 18px;
  align-items: center;
}

.game-area {
  position: relative;
  width: 100%;
  height: 90vh;
  overflow: hidden;
  cursor: pointer;
}

.balloon {
  position: absolute;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  transition: transform 0.2s ease-in-out;
  animation: floatUp 3s infinite ease-in-out;
}

@keyframes floatUp {
  0% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
  100% {
    transform: translateY(0);
  }
}

.game-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(255, 255, 255, 0.9);
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  z-index: 10;
  text-align: center;
}
</style>
