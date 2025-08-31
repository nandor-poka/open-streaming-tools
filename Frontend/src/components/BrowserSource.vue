<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import { SettingsStore } from '@/stores/SettingsStore'
import { UnitStore } from '@/stores/UnitStore'
import type { Axios } from 'axios'
import { onMounted, inject } from 'vue'
const unitStore = UnitStore()
const settingsStore = SettingsStore()
const axios: Axios = inject('axios') as Axios
onMounted(() => {
  axios
    .get('getSettings', {
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
    })
    .catch(function (error) {
      // handle error
      console.log(error)
    })
})

</script>

<template>
  <h1>Powered by OST</h1>
  <div class="parent">
    <div
      v-bind:style="{
        backgroundImage:
          'linear-gradient(to right, var(--ost-deck-fill-color) ' +
          unitStore.deck1VolumeData.volume +
          '% , var(--ost-deck-empty-color) ' +
          (unitStore.deck1VolumeData.volume > 50
            ? 100 - unitStore.deck1VolumeData.volume
            : unitStore.deck1VolumeData.volume) +
          '%)',
        color:
          'rgb( ' +
          settingsStore.sdRed +
          ',' +
          settingsStore.sdGreen +
          ',' +
          settingsStore.sdBlue +
          ') ',
      }"
      class="box"
    >
      <p class="deckNumber">Deck 1</p>
      <Transition name="slide-fade">
        <p
          v-if="unitStore.deck1UpdateSwtich == true"
          v-bind:style="{
            fontSize: unitStore.deck1SongData.trackTitle.length > 25 ? 'x-large' : 'xx-large',
          }"
          class="songTitle textWrap"
        >
          <b> {{ unitStore.deck1SongData.trackTitle }}</b>
        </p>
      </Transition>
      <Transition name="slide-fade">
        <p v-if="unitStore.deck1UpdateSwtich == true" class="artist">
          <b> {{ unitStore.deck1SongData.artistName }}</b>
        </p>
      </Transition>
    </div>

    <div
      v-bind:style="{
        backgroundImage:
          'linear-gradient(to left, var(--ost-deck-fill-color) ' +
          unitStore.deck2VolumeData.volume +
          '% , var(--ost-deck-empty-color) ' +
          (unitStore.deck2VolumeData.volume > 50
            ? 100 - unitStore.deck2VolumeData.volume
            : unitStore.deck2VolumeData.volume) +
          '%)',
        color:
          'rgb( ' +
          settingsStore.sdRed +
          ',' +
          settingsStore.sdGreen +
          ',' +
          settingsStore.sdBlue +
          ') ',
      }"
      class="box right-aligned"
    >
      <p class="deckNumber">Deck 2</p>
      <Transition name="slide-fade">
        <p
          v-if="unitStore.deck2UpdateSwtich == true"
          v-bind:style="{
            fontSize: unitStore.deck2SongData.trackTitle.length > 25 ? 'x-large' : 'xx-large',
          }"
          class="songTitle textWrap"
        >
          {{ unitStore.deck2SongData.trackTitle }}
        </p>
      </Transition>
      <Transition name="slide-fade">
        <p v-if="unitStore.deck2UpdateSwtich == true" class="artist">
          {{ unitStore.deck2SongData.artistName }}
        </p>
      </Transition>
    </div>
  </div>

  <div class="parent">
    <div
      v-bind:style="{
        backgroundImage:
          'linear-gradient(to right, var(--ost-deck-fill-color) ' +
          unitStore.deck3VolumeData.volume +
          '% , var(--ost-deck-empty-color) ' +
          (unitStore.deck3VolumeData.volume > 50
            ? 100 - unitStore.deck3VolumeData.volume
            : unitStore.deck3VolumeData.volume) +
          '%)',
        color:
          'rgb( ' +
          settingsStore.sdRed +
          ',' +
          settingsStore.sdGreen +
          ',' +
          settingsStore.sdBlue +
          ') ',
      }"
      class="box"
    >
      <p class="deckNumber">Deck 3</p>
      <Transition name="slide-fade">
        <p
          v-if="unitStore.deck3UpdateSwtich == true"
          v-bind:style="{
            fontSize: unitStore.deck3SongData.trackTitle.length > 25 ? 'x-large' : 'xx-large',
          }"
          class="songTitle textWrap"
        >
          {{ unitStore.deck3SongData.trackTitle }}
        </p>
      </Transition>
      <Transition name="slide-fade">
        <p v-if="unitStore.deck3UpdateSwtich == true" class="artist">
          {{ unitStore.deck3SongData.artistName }}
        </p>
      </Transition>
    </div>

    <div
      v-bind:style="{
        backgroundImage:
          'linear-gradient(to left, var(--ost-deck-fill-color) ' +
          unitStore.deck4VolumeData.volume +
          '% , var(--ost-deck-empty-color) ' +
          (unitStore.deck4VolumeData.volume > 50
            ? 100 - unitStore.deck4VolumeData.volume
            : unitStore.deck4VolumeData.volume) +
          '%)',
        color:
          'rgb( ' +
          settingsStore.sdRed +
          ',' +
          settingsStore.sdGreen +
          ',' +
          settingsStore.sdBlue +
          ') ',
      }"
      class="box right-aligned"
    >
      <p class="deckNumber">Deck 4</p>
      <Transition name="slide-fade">
        <p
          v-if="unitStore.deck4UpdateSwtich == true"
          v-bind:style="{
            fontSize: unitStore.deck4SongData.trackTitle.length > 25 ? 'x-large' : 'xx-large',
          }"
          class="songTitle textWrap"
        >
          {{ unitStore.deck4SongData.trackTitle }}
        </p>
      </Transition>
      <Transition name="slide-fade">
        <p v-if="unitStore.deck4UpdateSwtich == true" class="artist">
          {{ unitStore.deck4SongData.artistName }}
        </p>
      </Transition>
    </div>
  </div>
</template>

<style scoped>
.box {
  width: 450px;
  height: 150px;
  border: 1px solid black;
  padding: 10px;
  margin: 10px;
  color: #81d9ff;
}

.solid {
  background-color: rgb(0, 114, 4);
}
.right-aligned {
  margin-left: auto;
  margin-right: 0;
}

.songTitle {
  font-size: xx-large;
  font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial,
    sans-serif;
}
.parent {
  display: flex;
}

.deckNumber {
  font-size: x-large;
}

.textWrap {
  p {
    word-break: normal;
    white-space: normal;
    text-wrap-style: pretty;
  }
}

.slide-fade-enter-active {
  transition: all 0.5s ease-out;
}

.slide-fade-leave-active {
  transition: all 2s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(150px);
  opacity: 0;
}
</style>
