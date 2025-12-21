<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import Navbar from './Navbar.vue'
import { UnitStore } from '@/stores/UnitStore'
import { SettingsStore } from '@/stores/SettingsStore'
import TwitchClient from './TwitchClient.vue'
import type { Axios } from 'axios'
import { inject, onMounted } from 'vue'
const unitStore = UnitStore()

const settingsStore = SettingsStore()
const axios: Axios = inject('axios') as Axios



onMounted(() => {
  axios
    .get('api/getSettings', {
      method: 'get',
      headers: {
        'Content-Type': 'application/json',
      },
    })
    .then(function (response) {
      const settings = response.data
      settingsStore.showTrackDelay = settings.showTrackDelay
      settingsStore.volumeThreshold = settings.volumeThreshold
      settingsStore.sdRed = settings.sdRed
      settingsStore.sdGreen = settings.sdGreen
      settingsStore.sdBlue = settings.sdBlue
      settingsStore.faderRed = settings.faderRed
      settingsStore.faderGreen = settings.faderGreen
      settingsStore.faderBlue = settings.faderBlue
      settingsStore.channelUserName = settings.channelUserName
      settingsStore.botUserName = settings.botUserName
      settingsStore.clientIdFilePath = settings.clientIdFilePath
      settingsStore.clientSecretFilePath = settings.clientSecretFilePath
      settingsStore.twitchStatus = settings.twitchStatus
      settingsStore.versionString = settings.versionString
    })
    .catch(function (error) {
      // handle error
      console.log(error)
    })
})

</script>

<template>
  <TwitchClient/>
  <Navbar />
  <div class="greetings">
    <h1>Dashboard</h1>
  </div>
  <div>
    <ul>
      <li v-if="settingsStore.channelUserName == null">Broadcaster username is missing</li>
      <li v-if="settingsStore.botUserName == null">Bot username is missing</li>
      <li v-if="settingsStore.clientIdFilePath == null">Path for client ID file is missing</li>
      <li v-if="settingsStore.clientSecretFilePath == null">Path for client secret file is missing</li>
    </ul>
    <h2>Twitch credetials : {{ settingsStore.twitchStatus }} </h2>
    <h2>Twitch connection live: {{ settingsStore.twitchResponse }} </h2>
    <a href='https://id.twitch.tv/oauth2/authorize?client_id=n6breeyo2zy1nzlpfx43x91lgaobgo&force_verify=true&response_type=code&redirect_uri=http://localhost:8080/api/twitchBot&scope=user%3Abot%20user%3Awrite%3Achat'>Connect to Twitch Bot user</a>
    <a href='https://id.twitch.tv/oauth2/authorize?client_id=n6breeyo2zy1nzlpfx43x91lgaobgo&force_verify=true&response_type=code&redirect_uri=http://localhost:8080/api/twitchBroadcaster&scope=user%3Abot%20user%3Aread%3Achat%20channel%3Amanage%3Aredemptions%20channel%3Aread%3Aredemptions'>Connect to Twitch Boradcasting user</a>
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

li {
  color: red;
  font-weight: 300;
  font-size: 2rem;
}
</style>
