<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import { UnitStore } from '@/stores/UnitStore'
const unitStore = UnitStore()
</script>

<template>
  <div class="parent">
    <div
      v-bind:style="{
        backgroundImage:
          'linear-gradient(to right, rgba(0, 114, 4, 1) ' +
          unitStore.deck1VolumeData.volume +
          '% , rgba(255, 255, 255, 0) ' +
          (unitStore.deck1VolumeData.volume > 50
            ? 100 - unitStore.deck1VolumeData.volume
            : unitStore.deck1VolumeData.volume) +
          '%)',
      }"
      class="box"
    >
      <p class="deckNumber">Deck 1</p>
      <p
        v-bind:style="{
          fontSize: unitStore.deck1SongData.trackTitle.length > 25 ? 'x-large' : 'xx-large',
        }"
        class="songTitle textWrap"
      >
        {{ unitStore.deck1SongData.trackTitle }}
      </p>

      <p class="artist">
        {{ unitStore.deck1SongData.artistName }}
      </p>
    </div>

    <div
      v-bind:style="{
        backgroundImage:
          'linear-gradient(to left, rgba(0, 114, 4, 1) ' +
          unitStore.deck2VolumeData.volume +
          '% , rgba(255, 255, 255, 0) ' +
          (unitStore.deck2VolumeData.volume > 50
            ? 100 - unitStore.deck2VolumeData.volume
            : unitStore.deck2VolumeData.volume) +
          '%)',
      }"
      class="box right-aligned"
    >
      <p class="deckNumber">Deck 2</p>
      <p
        v-bind:style="{
          fontSize: unitStore.deck2SongData.trackTitle.length > 25 ? 'x-large' : 'xx-large',
        }"
        class="songTitle textWrap"
      >
        {{ unitStore.deck2SongData.trackTitle }}
      </p>
      <p class="artist">{{ unitStore.deck2SongData.artistName }}</p>
    </div>
  </div>

  <div class="parent">
    <div
      v-bind:style="{
        backgroundImage:
          'linear-gradient(to right, rgba(0, 114, 4, 1) ' +
          unitStore.deck3VolumeData.volume +
          '% , rgba(255, 255, 255, 0) ' +
          (unitStore.deck3VolumeData.volume > 50
            ? 100 - unitStore.deck3VolumeData.volume
            : unitStore.deck3VolumeData.volume) +
          '%)',
      }"
      class="box"
    >
      <p class="deckNumber">Deck 3</p>
      <p
        v-bind:style="{
          fontSize: unitStore.deck3SongData.trackTitle.length > 25 ? 'x-large' : 'xx-large',
        }"
        class="songTitle textWrap"
      >
        {{ unitStore.deck3SongData.trackTitle }}
      </p>
      <p class="artist">{{ unitStore.deck3SongData.artistName }}</p>
    </div>

    <div
      v-bind:style="{
        backgroundImage:
          'linear-gradient(to left, rgba(0, 114, 4, 1) ' +
          unitStore.deck4VolumeData.volume +
          '% , rgba(255, 255, 255, 0) ' +
          (unitStore.deck4VolumeData.volume > 50
            ? 100 - unitStore.deck4VolumeData.volume
            : unitStore.deck4VolumeData.volume) +
          '%)',
      }"
      class="box right-aligned"
    >
      <p class="deckNumber">Deck 4</p>
      <p
        v-bind:style="{
          fontSize: unitStore.deck4SongData.trackTitle.length > 25 ? 'x-large' : 'xx-large',
        }"
        class="songTitle textWrap"
      >
        {{ unitStore.deck4SongData.trackTitle }}
      </p>
      <p class="artist">{{ unitStore.deck4SongData.artistName }}</p>
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
  transform: translateX(50px);
  opacity: 0;
}
</style>
