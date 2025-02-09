<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import { stageLinQStore } from '@/stores/stagelinqStore'
const stagelingqstore = stageLinQStore()
</script>

<template>
  <div class="parent">
    <div
      v-bind:style="{
        backgroundImage:
          'linear-gradient(to right, rgba(0, 114, 4, 1) ' +
          (stagelingqstore.deck1State.faderPos > 50
            ? stagelingqstore.deck1State.faderPos
            : 100 - stagelingqstore.deck1State.faderPos) +
          '% , rgba(255, 255, 255, 0) ' +
          (stagelingqstore.deck1State.faderPos > 50
            ? 100 - stagelingqstore.deck1State.faderPos
            : stagelingqstore.deck1State.faderPos) +
          '%)',
      }"
      class="box"
    >
      <p class="deckNumber">Deck 1</p>
      <p
        v-bind:style="{
          fontSize: stagelingqstore.deck1State.trackTitle.length > 25 ? 'x-large' : 'xx-large',
        }"
        class="songTitle textWrap"
      >
        {{ stagelingqstore.deck1State.trackTitle }}
      </p>
      <p class="artist">{{ stagelingqstore.deck1State.artistName }}</p>
    </div>

    <div
      v-bind:style="{
        backgroundImage:
          'linear-gradient(to left, rgba(0, 114, 4, 1) ' +
          (stagelingqstore.deck2State.faderPos > 50
            ? stagelingqstore.deck2State.faderPos
            : 100 - stagelingqstore.deck2State.faderPos) +
          '% , rgba(255, 255, 255, 0) ' +
          (stagelingqstore.deck2State.faderPos > 50
            ? 100 - stagelingqstore.deck2State.faderPos
            : stagelingqstore.deck2State.faderPos) +
          '%)',
      }"
      class="box right-aligned"
    >
      <p class="deckNumber">Deck 2</p>
      <p
        v-bind:style="{
          fontSize: stagelingqstore.deck2State.trackTitle.length > 25 ? 'x-large' : 'xx-large',
        }"
        class="songTitle textWrap"
      >
        {{ stagelingqstore.deck2State.trackTitle }}
      </p>
      <p class="artist">{{ stagelingqstore.deck2State.artistName }}</p>
    </div>
  </div>

  <div class="parent">
    <div
      v-bind:style="{
        backgroundImage:
          'linear-gradient(to right, rgba(0, 114, 4, 1) ' +
          (stagelingqstore.deck3State.faderPos > 50
            ? stagelingqstore.deck3State.faderPos
            : 100 - stagelingqstore.deck3State.faderPos) +
          '% , rgba(255, 255, 255, 0) ' +
          (stagelingqstore.deck3State.faderPos > 50
            ? 100 - stagelingqstore.deck3State.faderPos
            : stagelingqstore.deck3State.faderPos) +
          '%)',
      }"
      class="box"
    >
      <p class="deckNumber">Deck 3</p>
      <p
        v-bind:style="{
          fontSize: stagelingqstore.deck3State.trackTitle.length > 25 ? 'x-large' : 'xx-large',
        }"
        class="songTitle textWrap"
      >
        {{ stagelingqstore.deck3State.trackTitle }}
      </p>
      <p class="artist">{{ stagelingqstore.deck3State.artistName }}</p>
    </div>

    <div
      v-bind:style="{
        backgroundImage:
          'linear-gradient(to left, rgba(0, 114, 4, 1) ' +
          (stagelingqstore.deck4State.faderPos > 50
            ? stagelingqstore.deck4State.faderPos
            : 100 - stagelingqstore.deck4State.faderPos) +
          '% , rgba(255, 255, 255, 0) ' +
          (stagelingqstore.deck4State.faderPos > 50
            ? 100 - stagelingqstore.deck4State.faderPos
            : stagelingqstore.deck4State.faderPos) +
          '%)',
      }"
      class="box right-aligned"
    >
      <p class="deckNumber">Deck 4</p>
      <p
        v-bind:style="{
          fontSize: stagelingqstore.deck4State.trackTitle.length > 25 ? 'x-large' : 'xx-large',
        }"
        class="songTitle textWrap"
      >
        {{ stagelingqstore.deck4State.trackTitle }}
      </p>
      <p class="artist">{{ stagelingqstore.deck4State.artistName }}</p>
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
  font-size: large;
}

.textWrap {
  p {
    word-break: normal;
    white-space: normal;
    text-wrap-style: pretty;
  }
}
</style>
