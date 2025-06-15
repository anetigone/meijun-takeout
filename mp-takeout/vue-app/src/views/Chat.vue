<template>
  <div class="chat-gradient-bg">
    <h2 class="chat-title">WebSocket 聊天室</h2>
    <ChatWebSocket />
  </div>
</template>

<script setup lang="ts">
import ChatWebSocket from '../components/ChatWebSocket.vue'
</script>

<style scoped>
.chat-gradient-bg {
  min-height: 100vh;
  width: 100vw;
  background: linear-gradient(120deg, #a18cd1 0%, #fbc2eb 25%, #fad0c4 50%, #ffd6e0 75%, #a1c4fd 100%);
  background-size: 200% 200%;
  animation: chatGradientMove 16s ease-in-out infinite;
}
@keyframes chatGradientMove {
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
.chat-title {
  text-align: center;
  margin-top: 48px;
  margin-bottom: 32px;
  font-size: 2.2rem;
  font-weight: 700;
  letter-spacing: 2px;
  color: #7c3aed;
  text-shadow: 0 4px 24px rgba(161,140,209,0.18), 0 1.5px 0 #fff;
  background: linear-gradient(90deg, #a18cd1 10%, #fbc2eb 40%, #fad0c4 70%, #a1c4fd 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
</style>