<!-- Follower List Component -->
<script setup lang="ts">
import { FollowerStore } from '@/stores/FollowerStore'
import type { Axios } from 'axios'
import { inject } from 'vue'

const followerStore = FollowerStore()
const axios: Axios = inject('axios') as Axios

function acknowledgeFollower(id: string) {
  followerStore.acknowledgeFollower(id)
  // Optionally send to backend for persistence
  // axios.post('/api/acknowledgeFollower', { id })
}

function formatTime(date: Date): string {
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${hours}:${minutes}`
}
</script>

<template>
  <article class="follower-card card">
    <div class="card-header">
      <h3>Recent Followers</h3>
      <span class="follower-count">{{ followerStore.getUnacknowledgedFollowers().length }}</span>
    </div>

    <div class="follower-list">
      <div
        v-for="follower in followerStore.followers"
        :key="follower.id"
        class="follower-item"
        :class="{ acknowledged: follower.acknowledged }"
      >
        <div class="follower-info">
          <span class="follower-name">{{ follower.displayName }}</span>
          <span class="follower-time">{{ formatTime(follower.followedAt) }}</span>
        </div>
        <button
          v-if="!follower.acknowledged"
          class="btn-acknowledge"
          @click="acknowledgeFollower(follower.id)"
          aria-label="Acknowledge follower"
        >
          ✓
        </button>
      </div>

      <div v-if="followerStore.followers.length === 0" class="empty-state">
        <p>No recent followers</p>
      </div>
    </div>
  </article>
</template>

<style scoped>
.follower-card {
  background: #000;
  border: 3px solid var(--azure);
  padding: 1rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,152,255,0.2);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 0.75rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid var(--azure);
}

.card-header h3 {
  margin: 0;
  color: var(--sgbus-green);
}

.follower-count {
  background: var(--sgbus-green);
  color: black;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
}

.follower-list {
  max-height: 300px;
  overflow-y: auto;
}

.follower-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.5rem;
  margin-bottom: 0.25rem;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(0, 152, 255, 0.2);
  border-radius: 6px;
  transition: all 0.2s;
}

.follower-item.acknowledged {
  opacity: 0.6;
  background: rgba(46, 213, 115, 0.1);
  border-color: var(--sgbus-green);
}

.follower-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.follower-name {
  color: var(--azure);
  font-weight: 600;
}

.follower-time {
  color: #888;
  font-size: 0.8rem;
}

.btn-acknowledge {
  background: var(--sgbus-green);
  color: black;
  border: none;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  cursor: pointer;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.btn-acknowledge:hover {
  background: var(--azure);
  color: white;
  transform: scale(1.1);
}

.empty-state {
  text-align: center;
  color: #666;
  padding: 2rem;
}

.empty-state p {
  margin: 0;
}
</style>
