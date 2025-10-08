<script setup lang="ts">
import { Client } from '@stomp/stompjs'
import { UnitStore } from '@/stores/UnitStore'
import type { SongData } from '@/types/SongData'
import type { ChannelVolumeData } from '@/types/ChannelVolumeData'
import type { Unit } from './types/Unit'
import { TrackStore } from './stores/TrackStore'
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
