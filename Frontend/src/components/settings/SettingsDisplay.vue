<script setup lang="ts">
import { SettingsStore } from '@/stores/SettingsStore'
const settingsStore = SettingsStore()
</script>

<template>
  <h2>Display / Colors & Animation</h2>
  <hr />
  <div class="parent">
    <div class="box">
      <label for="volumeSlider">Volume slider tester</label>
      <input
        ref="volSliderRef"
        v-model="settingsStore.volumeSliderValue"
        type="range"
        min="0"
        max="100"
        class="slider"
        id="volumeSlider"
      />
      <div>
        <p>Fader level indicator color</p>
        <p>
          <label for="faderRed">Red</label>
          <input v-model="settingsStore.faderRed" type="range" min="0" max="255" class="slider" id="faderRed" />
        </p>
        <p>
          <label for="faderGreen">Green</label>
          <input v-model="settingsStore.faderGreen" type="range" min="0" max="255" class="slider" id="faderGreen" />
        </p>
        <p>
          <label for="faderBlue">Blue</label>
          <input v-model="settingsStore.faderBlue" type="range" min="0" max="255" class="slider" id="sdblue" />
        </p>
      </div>
      <div>
        <p>Song data color</p>
        <p>
          <label for="sdred">Red</label>
          <input ref="sdRed" v-model="settingsStore.sdRed" type="range" min="0" max="255" class="slider" id="sdred" />
        </p>
        <p>
          <label for="sdgreen">Green</label>
          <input ref="sdGreen" v-model="settingsStore.sdGreen" type="range" min="0" max="255" class="slider" id="sdgreen" />
        </p>
        <p>
          <label for="sdblue">Blue</label>
          <input ref="sdBlue" v-model="settingsStore.sdBlue" type="range" min="0" max="255" class="slider" id="sdblue" />
        </p>
      </div>
      <div>
        <p>Song data transition settings</p>
        <p>
          <label for="animDuration"></label>
          <input ref="transitionDuration" type="number" step="0.1" min="0" max="5" id="animDuration" />
        </p>
        <p>
          <label for="animDisposition">Disposition in pixel</label>
          <input
            ref="animationDisposition"
            type="number"
            min="50"
            max="250"
            value="50"
            id="animDisposition"
          />
        </p>
      </div>
      <button type="button">Test animation</button>
      <p>
        <button ref="saveSettings" type="button">Save config</button>
      </p>
    </div>
    <div>
      <p>Sample song id display</p>
      <div
        v-bind:style="{
          backgroundImage:
            'linear-gradient(to right, rgb( ' +
              settingsStore.faderRed +',' +
              settingsStore.faderGreen +',' +
              settingsStore.faderBlue +') '  +
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
</template>

