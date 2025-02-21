import { ref } from 'vue'
import { defineStore } from 'pinia'
export const SettingsStore = defineStore('StageLinQSTore', () => {
  const showTrackDelay = ref(0)
  const volumeThreshold = ref(0)
  return {
    showTrackDelay,
    volumeThreshold,
  }
})
