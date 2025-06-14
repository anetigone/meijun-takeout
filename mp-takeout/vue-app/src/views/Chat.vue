<!-- filepath: g:\Documents\GitHub\meijun-takeout\mp-takeout\vue-app\src\views\ChatView.vue -->
<template>
  <div style="min-height: 100vh; background: #f0f2f5;">
    <h2 style="text-align: center; margin-top: 40px;">WebSocket 聊天室</h2>
    <ChatWebSocket />
  </div>
</template>

<script setup lang="ts">
import ChatWebSocket from '../components/ChatWebSocket.vue'
</script>