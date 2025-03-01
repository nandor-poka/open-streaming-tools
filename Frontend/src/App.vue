<script setup lang="ts">
import { Client } from '@stomp/stompjs'
import { UnitStore } from '@/stores/UnitStore'
import type { SongData } from '@/types/SongData'
import type { ChannelVolumeData } from '@/types/ChannelVolumeData'
import Axios from 'axios'
const axiosInstance = Axios.create()
axiosInstance.defaults.baseURL = 'http://localhost:8080/'

const unitStore = UnitStore()
const client = new Client({
  brokerURL: 'ws://localhost:8080/websocket',
  onConnect: () => {
    client.subscribe('/websocketData', (message) => {
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
          break
        case 'CHANNEL_VOLUME_DATA':
          const volumeData: ChannelVolumeData = {
            deckNum: msg.deckNumber,
            volume: msg.volume,
          }
          unitStore.updateVolumeData(volumeData)
          break
      }
    })
    client.publish({ destination: '/app/startup', body: 'Frontend running.' })
  },
})
client.activate()
</script>

<template>
  <main>
    <RouterView />
  </main>
</template>
