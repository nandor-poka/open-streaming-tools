<script setup lang="ts">
import Navbar from './Navbar.vue'
import { SettingsStore } from '@/stores/SettingsStore'
const settingsStore = SettingsStore()
</script>

<template>
  <Navbar />
  <h1>Settings</h1>
  <div>
    <p>
      <label for="showTrackDelay">Show track after info (seconds, 0 disables delay)</label>
      <input v-bind:value="settingsStore.showTrackDelay" type="number" id="showTrackDelay" />
    </p>
    <p>
      <label for="volumeThreshold"
        >Only show track info if volume is above (percent, 0 disables)</label
      >
      <input v-bind:value="settingsStore.volumeThreshold" type="number" id="volumeThreshold" />
    </p>
  </div>
  <button type="button">Save config</button>
</template>
