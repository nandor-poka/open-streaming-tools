<script setup lang="ts">
import { Client } from '@stomp/stompjs'
import { provide, inject } from 'vue'
import { UnitStore } from '@/stores/UnitStore'
import { ChatStore } from '@/stores/ChatStore'
import type { SongData } from '@/types/SongData'
import type { ChannelVolumeData } from '@/types/ChannelVolumeData'
import type { Unit } from './types/Unit'
import { TrackStore } from './stores/TrackStore'
import { SettingsStore } from '@/stores/SettingsStore'
import type { Axios } from 'axios'
import type {
  TwitchSessionWelcome,
  TwitchChatMessage,
  TwitchChatCommand,
  TwitchChannelPointsRedemption,
  TwitchConnectionStatus,
  TwitchError,
} from '@/types/twitch'
const unitStore = UnitStore()
const trackStore = TrackStore()
const settingsStore = SettingsStore()
const chatStore = ChatStore()
const axios: Axios = inject('axios') as Axios

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

    // Subscribe to Twitch events from the backend WebSocket
    ostClient.subscribe('/api/websocketData/twitch/session', (message: any) => {
      handleSessionWelcome(JSON.parse(message.body))
    })

    ostClient.subscribe('/api/websocketData/twitch/chat', (message: any) => {
      handleChatMessage(JSON.parse(message.body))
    })

    ostClient.subscribe('/api/websocketData/twitch/chat/command', (message: any) => {
      handleChatCommand(JSON.parse(message.body))
    })

    ostClient.subscribe('/api/websocketData/twitch/points', (message: any) => {
      handlePointsRedemption(JSON.parse(message.body))
    })

    ostClient.subscribe('/api/websocketData/twitch/status', (message: any) => {
      handleStatusUpdate(JSON.parse(message.body))
    })

    ostClient.subscribe('/api/websocketData/twitch/error', (message: any) => {
      handleError(JSON.parse(message.body))
    })

    ostClient.publish({ destination: '/app/startup', body: 'Frontend running.' })
  },
})
ostClient.activate()

// Provide the WebSocket client to all child components
provide('ostWebSocketClient', ostClient)

// Twitch event handlers
function handleSessionWelcome(message: TwitchSessionWelcome) {
  console.log('Session welcome:', message)
  settingsStore.twitchResponse = 'Session established: ' + message.sessionId
}

function handleChatMessage(message: TwitchChatMessage) {
  try {
    console.log('Chat message received:', message)
    const event = message.event
    if (!event) return

    const messageText = event.message?.text || ''
    const chatterName = event.chatter_user_name || event.chatter_user_login || 'Unknown'
    const chatterColor = event.chatter_user_color || undefined

    // Log chat activity
    console.debug(`[${chatterName}]: ${messageText}`)

    // Add to ChatStore for dashboard display
    chatStore.addMessage(chatterName, messageText, chatterColor)
  } catch (error) {
    console.error('Error handling chat message:', error)
  }
}

function handleChatCommand(message: TwitchChatCommand) {
  try {
    console.log('Chat command received:', message.command, message)

    switch (message.command) {
      case 'recommend':
        handleRecommendCommand(message.event)
        break
      case 'shoutout':
        handleShoutoutCommand(message.event)
        break
      default:
        console.warn('Unknown chat command:', message.command)
    }
  } catch (error) {
    console.error('Error handling chat command:', error)
  }
}

function handleRecommendCommand(event: any) {
  try {
    console.log('Processing !recommend command')
    // Get recommendation in current key
    axios.get('api/getInKeyRecommendation/' + trackStore.currentKey, {
      headers: {
        'Content-Type': 'application/json',
      },
    }).catch(function (error) {
      console.error('Error getting recommendation:', error)
    })
  } catch (error) {
    console.error('Error handling recommend command:', error)
  }
}

function handleShoutoutCommand(event: any) {
  try {
    console.log('Shoutout command processed on backend')
    // Shoutout is handled entirely on backend via TwitchUtils.sendToChat()
    // Frontend just logs or displays confirmation if needed
  } catch (error) {
    console.error('Error handling shoutout command:', error)
  }
}

function handlePointsRedemption(message: TwitchChannelPointsRedemption) {
  try {
    console.log('Points redemption:', message)
    // Handle custom reward redemptions here
  } catch (error) {
    console.error('Error handling points redemption:', error)
  }
}

function handleStatusUpdate(message: TwitchConnectionStatus) {
  console.log('Twitch connection status:', message.status)
  settingsStore.twitchResponse = 'Connection status: ' + message.status
}

function handleError(message: TwitchError) {
  console.error('Twitch error:', message.error)
  settingsStore.twitchResponse = 'Error: ' + message.error
}
</script>

<template>
  <main>
    <RouterView />
  </main>
</template>
