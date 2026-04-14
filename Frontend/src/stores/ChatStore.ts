import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface MessageFragment {
  type: 'text' | 'emote'
  content: string
  emoteId?: string
}

export interface ChatMessage {
  id: string
  username: string
  fragments: MessageFragment[]
  timestamp: Date
  color?: string
}

export const ChatStore = defineStore('chat', () => {
  const messages = ref<ChatMessage[]>([])
  const maxMessages = ref(100) // Keep last 100 messages in display

  function addMessage(username: string, fragments: MessageFragment[], color?: string) {
    const chatMessage: ChatMessage = {
      id: `${Date.now()}-${Math.random()}`,
      username,
      fragments,
      timestamp: new Date(),
      color,
    }

    messages.value.push(chatMessage)

    // Keep only the last maxMessages messages
    if (messages.value.length > maxMessages.value) {
      messages.value = messages.value.slice(-maxMessages.value)
    }
  }

  function clearMessages() {
    messages.value = []
  }

  function getMessages() {
    return messages.value
  }

  return {
    messages,
    maxMessages,
    addMessage,
    clearMessages,
    getMessages,
  }
})
