<!-- ChatWebSocket.vue -->
<template>
  <el-card style="max-width: 500px; margin: 40px auto;">
    <div style="margin-bottom: 16px;">
      <el-input v-model="uuid" placeholder="请输入你的UUID" style="width: 70%;" />
      <el-button type="primary" @click="connect" :disabled="connected" style="margin-left: 8px;">连接</el-button>
      <el-button @click="disconnect" :disabled="!connected" style="margin-left: 8px;">断开</el-button>
    </div>
    <el-divider />
    <div style="height: 200px; overflow-y: auto; background: #f5f5f5; padding: 8px; margin-bottom: 16px;">
      <div v-for="(msg, idx) in messages" :key="idx">
        <span :style="{color: msg.self ? '#409EFF' : '#67C23A'}">
          {{ msg.self ? '我' : '对方' }}: 
        </span>
        {{ msg.text }}
      </div>
    </div>
    <el-input
      v-model="input"
      placeholder="输入消息"
      @keyup.enter="send"
      :disabled="!connected"
      style="width: 80%;"
    />
    <el-button type="primary" @click="send" :disabled="!connected || !input" style="margin-left: 8px;">发送</el-button>
  </el-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const uuid = ref('')
const input = ref('')
const messages = ref<{ text: string; self: boolean }[]>([])
let ws: WebSocket | null = null
const connected = ref(false)

function connect() {
  if (!uuid.value) {
    ElMessage.warning('请输入UUID')
    return
  }
  ws = new WebSocket(`ws://localhost:8080/ws/${uuid.value}`)
  ws.onopen = () => {
    connected.value = true
    ElMessage.success('WebSocket已连接')
  }
  ws.onmessage = (event) => {
    messages.value.push({ text: event.data, self: false })
  }
  ws.onclose = () => {
    connected.value = false
    ElMessage.info('WebSocket已断开')
  }
  ws.onerror = () => {
    ElMessage.error('WebSocket连接出错')
    connected.value = false
  }
}

function send() {
  if (ws && connected.value && input.value) {
    ws.send(input.value)
    messages.value.push({ text: input.value, self: true })
    input.value = ''
  }
}

function disconnect() {
  if (ws) {
    ws.close()
    ws = null
  }
}
</script>