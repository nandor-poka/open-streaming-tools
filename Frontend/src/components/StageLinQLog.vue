<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import type { DeckState } from '@/types/DeckState'
import { Client } from '@stomp/stompjs'
import { stageLinQStore } from '@/stores/stagelinqStore'
import Navbar from './Navbar.vue'

const stagelingqstore = stageLinQStore()
const message = 'Log goes here'
const client = new Client({
  brokerURL: 'ws://localhost:8080/stagelinq',
  onConnect: () => {
    client.subscribe('/topic', (message) => {
      const msg = JSON.parse(message.body)
      stagelingqstore.stagelinQmessages.push(message.body)
      switch (msg.type) {
        case 'DECK_STATE':
          const deckState: DeckState = {
            deckNum: msg.deckNumber,
            trackTitle: msg.trackTitle,
            artistName: msg.artistName,
            tempo: msg.tempo,
            faderPos: msg.faderPos,
          }
          stagelingqstore.updateState(deckState)
          break
      }
    })
    client.publish({ destination: '/app/incoming', body: 'Hello Denon' })
  },
})
client.activate()
</script>

<template>
  <Navbar />
  <div>
    <h4 class="blue">{{ message }}</h4>
  </div>

  <div>
    <ul>
      <li v-for="mesg in stagelingqstore.stagelinQmessages" :key="mesg">
        {{ mesg }}
      </li>
    </ul>
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
