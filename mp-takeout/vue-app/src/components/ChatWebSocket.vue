<template>
  <div class="chatws-card-wrap">
    <el-card class="chatws-card">
      <div class="chatws-header">
        <div>
          <span class="chatws-label">UUID:</span>
          <span class="chatws-uuid">{{ uuid }}</span>
        </div>
        <div>
          <el-button type="primary" @click="connect" :disabled="connected">连接</el-button>
          <el-button @click="disconnect" :disabled="!connected" style="margin-left: 8px;">断开</el-button>
        </div>
      </div>
      <el-divider />
      <div class="chatws-messages">
        <div v-for="(msg, idx) in messages" :key="idx">
          <span :style="{color: msg.self ? '#409EFF' : '#67C23A'}">
            {{ msg.self ? '我' : '对方' }}: 
          </span>
          {{ msg.text }}
        </div>
      </div>
      <div class="chatws-input-row">
        <el-input
          v-model="input"
          placeholder="输入消息"
          @keyup.enter="send"
          :disabled="!connected"
          class="chatws-input"
        />
        <el-button type="primary" @click="send" :disabled="!connected || !input" class="chatws-send-btn">发送</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const uuid = ref('')
const input = ref('')
const messages = ref<{ text: string; self: boolean }[]>([])
let ws: WebSocket | null = null
const connected = ref(false)
const username = ref('')

function connect() {
  const uid = localStorage.getItem('uuid')
  const name = localStorage.getItem('username')
  if(!uid) {
    ElMessage.error('请先登录')
    return
  }
  uuid.value = uid
  username.value = name!

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

<style scoped>
.chatws-card-wrap {
  min-height: 80vh;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  padding-top: 40px;
}
.chatws-card {
  width: 100%;
  max-width: 500px;
  background: rgba(255,255,255,0.92);
  border-radius: 18px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.18);
  border: none;
}
.chatws-header {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.chatws-label {
  font-weight: bold;
  color: #7c3aed;
  letter-spacing: 1px;
}
.chatws-uuid {
  font-family: 'Fira Mono', 'Consolas', monospace;
  color: #333;
  background: #f3e8ff;
  padding: 2px 8px;
  border-radius: 6px;
  margin-left: 4px;
  font-size: 14px;
}
.chatws-messages {
  height: 200px;
  overflow-y: auto;
  background: rgba(245,245,245,0.95);
  padding: 8px;
  margin-bottom: 16px;
  border-radius: 8px;
  font-size: 15px;
}
.chatws-input-row {
  display: flex;
  align-items: center;
  gap: 8px;
}
.chatws-input {
  flex: 1;
}
.chatws-send-btn {
  min-width: 70px;
}
</style>