<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import Navbar from './Navbar.vue'
import { UnitStore } from '@/stores/UnitStore'
import { SettingsStore } from '@/stores/SettingsStore'
import { TrackStore } from '@/stores/TrackStore'
import { onMounted } from 'vue'
import { inject } from 'vue'
import type { Axios } from 'axios'
const axios: Axios = inject('axios') as Axios
const unitStore = UnitStore()
const settingsStore = SettingsStore()
const twitchClient = new WebSocket('wss://eventsub.wss.twitch.tv/ws')
const trackStore = TrackStore()
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
      settingsStore.updateTwitchStatus(settings.twitchStatus)
    })
    .catch(function (error) {
      // handle error
      console.log(error)
    })
})

twitchClient.onopen = ()=> {
    console.log("Websocket to Twitch opened")
  }
  twitchClient.onmessage = (weboscketMessage) =>{
    const twitchMessage = JSON.parse(weboscketMessage.data)
    console.log(weboscketMessage)
    console.log(twitchMessage)
    switch (twitchMessage.metadata.message_type) {
      case "session_welcome":
        axios.post('api/subscribeToTwtitch',{
          sessionId: twitchMessage.payload.session.id
        }).then(function(response){
          settingsStore.twitchResponse = response.data
        })
        .catch(function (error) {
          // handle error
          console.log(error)
        })
        console.log(twitchMessage.payload.session.id)
        break;
       case "notification":
        console.log(weboscketMessage)
        if (twitchMessage.payload.event.message.text == "!recommend"){
          axios
          .get('api/getInKeyRecommendation/'+trackStore.currentKey, {
            headers: {
              'Content-Type': 'application/json',
            },
          })
          .catch(function (error) {
            // handle error
            console.log(error)
          })
        }
        break;
      default:
        break;
    }
  }
</script>

<template>
  <Navbar />
  <div class="greetings">
    <h1>Dashboard</h1>
  </div>
  <div>
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
