<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import Navbar from './Navbar.vue'
import { UnitStore } from '@/stores/UnitStore'
import { SettingsStore } from '@/stores/SettingsStore'

const unitStore = UnitStore()
const settingsStore = SettingsStore()


</script>

<template>
  <Navbar />
  <div class="greetings">
    <h1>Dashboard</h1>
  </div>
  <div>
    <h2>Twitch credetials : {{ settingsStore.twitchStatus }} </h2>
    <h2>Twitch connection live: {{ settingsStore.twitchResponse }} </h2>
    <a href='https://id.twitch.tv/oauth2/authorize?client_id=n6breeyo2zy1nzlpfx43x91lgaobgo&force_verify=true&response_type=code&redirect_uri=http://localhost:8080/api/twitch&scope=user%3Abot%20user%3Aread%3Achat%20user%3Awrite%3Achat'>Login to Twitch</a>
  <!--user%3Abot%20 -->
  </div>
  <div>
    <h2>Current Device / Software</h2>
    <h3>{{ unitStore.currentUnit.longName }} version {{ unitStore.currentUnit.version }}</h3>
  </div>
</template>

<style scoped>
h1 {
  font-weight: 500;
  font-size: 2.6rem;
  position: relative;
  top: -10px;
}

h3 {
  font-size: 1.2rem;
}

.greetings h1,
.greetings h3 {
  text-align: center;
}

@media (min-width: 1024px) {
  .greetings h1,
  .greetings h3 {
    text-align: left;
  }
}
</style>
