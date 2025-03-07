import { ref } from 'vue'
import { defineStore } from 'pinia'
export const SettingsStore = defineStore('SettingsStore', () => {
  const showTrackDelay = ref(0)
  const volumeThreshold = ref(0)
  const volumeSliderValue = ref(50)
  const sdRed = ref(0)
  const sdGreen = ref(0)
  const sdBlue = ref(0)
  return {
    showTrackDelay,
    volumeThreshold,
    volumeSliderValue,
    sdRed,
    sdGreen,
    sdBlue,
  }
})
