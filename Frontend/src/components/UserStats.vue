<!-- User Stats Component -->
<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { ChatStore } from '@/stores/ChatStore'

interface UserStats {
  username: string
  firstSeen: Date
  lastSeen: Date
  messageCount: number
  activeTime: number // in minutes
}

const chatStore = ChatStore()
const userStats = ref<UserStats[]>([])
const updateInterval = ref<number | null>(null)

function updateUserStats() {
  const now = new Date()
  const activeThreshold = 5 * 60 * 1000 // 5 minutes ago

  // Get all messages from the last 5 minutes
  const recentMessages = chatStore.messages.filter(msg =>
    now.getTime() - msg.timestamp.getTime() < activeThreshold
  )

  // Group by username
  const userMap = new Map<string, UserStats>()

  recentMessages.forEach(msg => {
    const existing = userMap.get(msg.username)
    if (existing) {
      existing.messageCount++
      existing.lastSeen = msg.timestamp
    } else {
      userMap.set(msg.username, {
        username: msg.username,
        firstSeen: msg.timestamp,
        lastSeen: msg.timestamp,
        messageCount: 1,
        activeTime: 0
      })
    }
  })

  // Calculate active time for each user
  userMap.forEach(stats => {
    const timeDiff = stats.lastSeen.getTime() - stats.firstSeen.getTime()
    stats.activeTime = Math.max(1, Math.round(timeDiff / (60 * 1000))) // at least 1 minute
  })

  // Sort by active time descending
  userStats.value = Array.from(userMap.values()).sort((a, b) => b.activeTime - a.activeTime)
}

function formatActiveTime(minutes: number): string {
  if (minutes < 60) {
    return `${minutes}m`
  }
  const hours = Math.floor(minutes / 60)
  const mins = minutes % 60
  return `${hours}h ${mins}m`
}

onMounted(() => {
  updateUserStats()
  // @ts-ignore
  updateInterval.value = setInterval(updateUserStats, 60000) // Update every minute
})

onBeforeUnmount(() => {
  if (updateInterval.value) {
    clearInterval(updateInterval.value)
  }
})
</script>

<template>
  <article class="user-stats-card card">
    <div class="card-header">
      <h3>Active Chat Users</h3>
      <span class="user-count">{{ userStats.length }}</span>
    </div>

    <div class="user-stats-list">
      <div
        v-for="user in userStats.slice(0, 10)"
        :key="user.username"
        class="user-stat-item"
      >
        <div class="user-info">
          <span class="username">{{ user.username }}</span>
          <span class="message-count">{{ user.messageCount }} msg{{ user.messageCount !== 1 ? 's' : '' }}</span>
        </div>
        <span class="active-time">{{ formatActiveTime(user.activeTime) }}</span>
      </div>

      <div v-if="userStats.length === 0" class="empty-state">
        <p>No active users</p>
        <p class="text-muted">Users will appear here as they chat</p>
      </div>
    </div>
  </article>
</template>

<style scoped>
.user-stats-card {
  background: #000;
  border: 3px solid var(--sgbus-green);
  padding: 1rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(46, 213, 115, 0.2);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 0.75rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid var(--sgbus-green);
}

.card-header h3 {
  margin: 0;
  color: var(--azure);
}

.user-count {
  background: var(--azure);
  color: black;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
}

.user-stats-list {
  max-height: 300px;
  overflow-y: auto;
}

.user-stat-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.5rem;
  margin-bottom: 0.25rem;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(46, 213, 115, 0.2);
  border-radius: 6px;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.username {
  color: var(--sgbus-green);
  font-weight: 600;
}

.message-count {
  color: #888;
  font-size: 0.8rem;
}

.active-time {
  color: var(--azure);
  font-weight: 600;
  font-size: 0.9rem;
}

.empty-state {
  text-align: center;
  color: #666;
  padding: 2rem;
}

.empty-state p {
  margin: 0.25rem 0;
}

.text-muted {
  color: #555;
  font-size: 0.85rem;
}
</style>

