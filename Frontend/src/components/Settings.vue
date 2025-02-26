<script setup lang="ts">
import Navbar from './Navbar.vue'
import { SettingsStore } from '@/stores/SettingsStore'
import { onMounted, useTemplateRef } from 'vue'
const volSlider = useTemplateRef('volSliderRef')
const songDataRed = useTemplateRef('sdRed')
const songDataGreen = useTemplateRef('sdGreen')
const songDataBlue = useTemplateRef('sdBlue')

onMounted(() => {
  volSlider.value.oninput = function () {
    settingsStore.volumeSliderValue = parseInt(volSlider.value.value)
  }
  songDataRed.value.oninput = function () {
    settingsStore.sdRed = parseInt(songDataRed.value.value)
  }
  songDataGreen.value.oninput = function () {
    settingsStore.sdGreen = parseInt(songDataGreen.value.value)
  }
  songDataBlue.value.oninput = function () {
    settingsStore.sdBlue = parseInt(songDataBlue.value.value)
  }
})

const settingsStore = SettingsStore()
</script>

<template>
  <Navbar />
  <h1>Settings</h1>
  <div>
    <table>
      <tbody>
        <tr>
          <th>Setting</th>
          <th>Value</th>
          <th>Comments</th>
        </tr>
        <tr>
          <td>Show track after info</td>
          <td>
            <input v-bind:value="settingsStore.showTrackDelay" type="number" id="showTrackDelay" />
          </td>
          <td>
            seconds, 0 disables delay, meaning information will be shown immediately after loading
            the track or on any other condition fulfilment
          </td>
        </tr>
        <tr>
          <td>Only show track info if volume is above (percent, 0 disables)</td>
          <td>
            <input
              v-bind:value="settingsStore.volumeThreshold"
              type="number"
              id="volumeThreshold"
            />
          </td>
          <td>
            percent, this takes into consideration cross-fader position as well, 0 disables this
            condition, meaning that track info will be shown immedaitely after track loading or any
            other condition fulfilment
          </td>
        </tr>
      </tbody>
    </table>
  </div>
  <hr />
  <div class="parent">
    <div class="box">
      <label for="volumeSlider">Volume slider tester</label>
      <input ref="volSliderRef" type="range" min="0" max="100" class="slider" id="volumeSlider" />
      <div>
        <p>Song data color</p>
        <label for="sdred">Red</label>
        <input ref="sdRed" type="range" min="0" max="255" class="slider" id="sdred" />
        <label for="sdgreen">Green</label>
        <input ref="sdGreen" type="range" min="0" max="255" class="slider" id="sdgreen" />
        <label for="sdblue">Blue</label>
        <input ref="sdBlue" type="range" min="0" max="255" class="slider" id="sdblue" />
      </div>
    </div>
    <div>
      <p>Sample song id display</p>
      <div
        v-bind:style="{
          backgroundImage:
            'linear-gradient(to right, var(--ost-deck-fill-color) ' +
            settingsStore.volumeSliderValue +
            '% , var(--ost-deck-empty-color) ' +
            (settingsStore.volumeSliderValue > 50
              ? 100 - settingsStore.volumeSliderValue
              : settingsStore.volumeSliderValue) +
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

        <p class="songTitle textWrap">
          <b> Track title </b>
        </p>

        <p class="artist">
          <b> Artist</b>
        </p>
      </div>
    </div>
  </div>

  <button type="button">Save config</button>
</template>

<style lang="css">
table {
  max-width: 75%;
}
td {
  padding: 5px;
}

hr {
  border: 1px solid var(--safety-orange);
}
</style>
