<script setup lang="ts">
import { Client } from '@stomp/stompjs'
import { UnitStore } from '@/stores/UnitStore'
import type { SongData } from '@/types/SongData'
import type { ChannelVolumeData } from '@/types/ChannelVolumeData'
import type { Unit } from './types/Unit'
import { TrackStore } from './stores/TrackStore'
import { SettingsStore } from '@/stores/SettingsStore'
import type { Axios } from 'axios'
import { inject, onMounted } from 'vue'
const axios: Axios = inject('axios') as Axios
const settingsStore = SettingsStore()
//const axiosInstance = Axios.create()
//axiosInstance.defaults.baseURL = 'http://localhost:8080/'
//const twitchClient = new WebSocket('wss://eventsub.wss.twitch.tv/ws')
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
    })
    .catch(function (error) {
      // handle error
      console.log(error)
    })
})

const unitStore = UnitStore()
const trackStore = TrackStore()
const ostClient = new Client({
  brokerURL: 'ws://localhost:8080/api/websocket',
  onConnect: () => {
    ostClient.subscribe('/api/websocketData', (message) => {
      const msg = JSON.parse(message.body)
      unitStore.stagelinQmessages.push(message.body)

      switch (msg.type) {
        case 'SONG_DATA':
          const songData: SongData = {
            deckNum: msg.deckNumber,
            trackTitle: msg.trackTitle,
            artistName: msg.artistName,
          }
          unitStore.updateSongData(songData)
          if (msg.key > 0){
            trackStore.currentKey = msg.key
          }
          break
        case 'CHANNEL_VOLUME_DATA':
          const volumeData: ChannelVolumeData = {
            deckNum: msg.deckNumber,
            volume: msg.volume,
          }
          unitStore.updateVolumeData(volumeData)
          break
        case "STAGELINQ_DISCOVERY_MESSAGE":
          ostClient.publish({destination:'/app/getUnit', body: JSON.stringify(msg.deviceID)})
          break
        case "UNIT_DATA":
          const unit : Unit = {
              type: msg.unit.type,
              longName: msg.unit.longName,
              version: msg.unit.version,
              deckCount: msg.unit.deckCount
          }
          unitStore.updateUnit(unit)
          break
      }
    })
    ostClient.publish({ destination: '/app/startup', body: 'Frontend running.' })
  },
})
ostClient.activate()



</script>

<template>
  <main>
    <RouterView />
  </main>
</template>
