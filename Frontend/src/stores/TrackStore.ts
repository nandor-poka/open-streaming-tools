import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { Track } from '@/types/Track'

export const TrackStore = defineStore('TrackStore', () => {
  const currentKey = ref(1)
  const tracks = ref(Array<Track>())
  return {
    tracks,
    currentKey
  }
})
