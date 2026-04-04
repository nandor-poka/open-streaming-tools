<!-- Twitch Chat Display Component -->
<script setup lang="ts">
import { ref, inject, computed, watch, nextTick, onMounted } from 'vue'
import { ChatStore } from '@/stores/ChatStore'
import type { Client } from '@stomp/stompjs'

const chatStore = ChatStore()
const ostClient = inject('ostWebSocketClient') as Client
const messageInput = ref('')
const isConnected = ref(false)
const chatContainer = ref<HTMLDivElement | null>(null)

// Auto-scroll to bottom when new messages arrive
watch(
  () => chatStore.messages.length,
  async () => {
    await nextTick()
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight
    }
  }
)

// Check connection status
onMounted(() => {
  if (ostClient) {
    isConnected.value = ostClient.connected
  }
})

const formattedTime = computed(() => {
  return (date: Date) => {
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    return `${hours}:${minutes}`
  }
})

function sendMessage() {
  if (!messageInput.value.trim()) return

  try {
    // Send message to backend via STOMP
    if (ostClient && ostClient.connected) {
      ostClient.publish({
        destination: '/app/twitch/send-message',
        body: JSON.stringify({
          message: messageInput.value,
        }),
      })

      // Clear input
      messageInput.value = ''
    } else {
      console.warn('WebSocket client not connected')
    }
  } catch (error) {
    console.error('Error sending chat message:', error)
  }
}

function handleKeyPress(event: KeyboardEvent) {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    sendMessage()
  }
}

function clearChat() {
  chatStore.clearMessages()
}

const messageCount = computed(() => chatStore.messages.length)
</script>

<template>
  <article class="chat-card card">
    <div class="chat-header">
      <h3>Twitch Chat</h3>
      <div class="chat-status">
        <span :class="['status-badge', isConnected ? 'connected' : 'disconnected']">
          {{ isConnected ? 'Live' : 'Offline' }}
        </span>
        <span class="message-count">({{ messageCount }})</span>
      </div>
    </div>

    <div class="chat-messages" ref="chatContainer">
      <template v-if="messageCount === 0">
        <div class="empty-state">
          <p>No messages yet...</p>
          <p class="text-muted">Chat messages will appear here</p>
        </div>
      </template>
      <template v-else>
        <div
          v-for="msg in chatStore.messages"
          :key="msg.id"
          class="chat-message"
        >
          <span class="message-time">{{ formattedTime(msg.timestamp) }}</span>
          <span
            class="message-username"
            :style="msg.color ? { color: msg.color } : {}"
          >
            {{ msg.username }}:
          </span>
          <span class="message-text">{{ msg.message }}</span>
        </div>
      </template>
    </div>

    <div class="chat-input-area">
      <textarea
        v-model="messageInput"
        class="chat-input"
        placeholder="Send a message to chat..."
        @keypress="handleKeyPress"
        aria-label="Chat message input"
      />
      <div class="chat-actions">
        <button
          class="btn btn-small btn-primary"
          @click="sendMessage"
          :disabled="!messageInput.trim() || !isConnected"
          aria-label="Send message"
        >
          Send
        </button>
        <button
          class="btn btn-small btn-secondary"
          @click="clearChat"
          aria-label="Clear chat"
        >
          Clear
        </button>
      </div>
    </div>
  </article>
</template>

<style scoped>
.chat-card {
  display: flex;
  flex-direction: column;
  height: 500px;
  max-height: 80vh;
}

.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 0 0 0.75rem 0;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid var(--azure);
}

.chat-header h3 {
  margin: 0;
  color: var(--sgbus-green);
}

.chat-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.status-badge {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.25rem 0.5rem;
  text-indent: -9999px;
  position: relative;
}

.status-badge.connected::before {
  content: '●';
  color: var(--sgbus-green);
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  text-indent: 0;
}

.status-badge.disconnected::before {
  content: '●';
  color: #ff6b6b;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  text-indent: 0;
}

.message-count {
  color: var(--azure);
  font-size: 0.85rem;
  font-weight: 500;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 0.75rem;
  padding: 0.75rem;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(0, 152, 255, 0.2);
  border-radius: 6px;
}

.chat-messages::-webkit-scrollbar {
  width: 8px;
}

.chat-messages::-webkit-scrollbar-track {
  background: rgba(0, 152, 255, 0.1);
  border-radius: 4px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: var(--azure);
  border-radius: 4px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: var(--sgbus-green);
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #666;
  text-align: center;
}

.empty-state p {
  margin: 0.25rem 0;
}

.text-muted {
  color: #555;
  font-size: 0.85rem;
}

.chat-message {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  word-break: break-word;
  line-height: 1.4;
}

.message-time {
  color: #888;
  font-size: 0.75rem;
  font-weight: 500;
  min-width: 45px;
  flex-shrink: 0;
}

.message-username {
  color: var(--azure);
  font-weight: 600;
  flex-shrink: 0;
}

.message-text {
  color: var(--sgbus-green);
  flex: 1;
}

.chat-input-area {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.chat-input {
  padding: 0.75rem;
  background: rgba(0, 0, 0, 0.5);
  border: 1px solid var(--azure);
  border-radius: 6px;
  color: var(--sgbus-green);
  font-family: inherit;
  font-size: 0.95rem;
  resize: none;
  max-height: 80px;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.chat-input:focus {
  outline: none;
  border-color: var(--sgbus-green);
  box-shadow: 0 0 8px rgba(46, 213, 115, 0.2);
}

.chat-actions {
  display: flex;
  gap: 0.5rem;
}

.btn-small {
  padding: 0.5rem 0.75rem;
  font-size: 0.9rem;
  border-radius: 4px;
  border: 1px solid var(--azure);
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s;
}

.btn-small.btn-primary {
  background: var(--azure);
  color: white;
  border-color: var(--azure);
}

.btn-small.btn-primary:hover:not(:disabled) {
  background: var(--sgbus-green);
  border-color: var(--sgbus-green);
  color: black;
}

.btn-small.btn-primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-small.btn-secondary {
  background: transparent;
  color: var(--sgbus-green);
  border-color: var(--sgbus-green);
}

.btn-small.btn-secondary:hover {
  background: rgba(46, 213, 115, 0.1);
  color: var(--azure);
  border-color: var(--azure);
}
</style>

