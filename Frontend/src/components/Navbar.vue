<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import { SettingsStore } from '@/stores/SettingsStore'
const settingsStore = SettingsStore()
</script>
<template>
  <h1>Open Streaming Tools {{ settingsStore.versionString }}</h1>
  <nav>
    <RouterLink to="/">Dashboard</RouterLink>
    <RouterLink to="/playlists">Playlists</RouterLink>
    <RouterLink to="/log">Log</RouterLink>
    <RouterLink to="/settings">Settings</RouterLink>
    <RouterLink to="/chatbot">Chatbot</RouterLink>
    <RouterLink to="/browsersource">BrowserSource</RouterLink>
  </nav>
</template>
