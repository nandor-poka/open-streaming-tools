import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface Follower {
  id: string
  username: string
  displayName: string
  followedAt: Date
  acknowledged: boolean
}

export const FollowerStore = defineStore('follower', () => {
  const followers = ref<Follower[]>([])
  const maxFollowers = ref(10) // Keep last 10 followers

  function addFollower(username: string, displayName: string, followedAt: Date) {
    const follower: Follower = {
      id: `${Date.now()}-${Math.random()}`,
      username,
      displayName,
      followedAt,
      acknowledged: false,
    }

    followers.value.unshift(follower) // Add to beginning

    // Keep only the last maxFollowers followers
    if (followers.value.length > maxFollowers.value) {
      followers.value = followers.value.slice(0, maxFollowers.value)
    }
  }

  function acknowledgeFollower(id: string) {
    const follower = followers.value.find(f => f.id === id)
    if (follower) {
      follower.acknowledged = true
    }
  }

  function getUnacknowledgedFollowers() {
    return followers.value.filter(f => !f.acknowledged)
  }

  function getAllFollowers() {
    return followers.value
  }

  function clearFollowers() {
    followers.value = []
  }

  return {
    followers,
    maxFollowers,
    addFollower,
    acknowledgeFollower,
    getUnacknowledgedFollowers,
    getAllFollowers,
    clearFollowers,
  }
})
