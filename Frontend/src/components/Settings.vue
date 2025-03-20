<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import Navbar from './Navbar.vue'
import { SettingsStore } from '@/stores/SettingsStore'
import type { Axios } from 'axios'
import { inject, onMounted, useTemplateRef } from 'vue'
/* const volSlider = useTemplateRef('volSliderRef')
const songDataRed = useTemplateRef('sdRed')
const songDataGreen = useTemplateRef('sdGreen')
const songDataBlue = useTemplateRef('sdBlue') */
const settingsStore = SettingsStore()
const saveSettingButton = useTemplateRef("saveSettings")
const axios = inject("axios") as Axios
onMounted(() => {

  if(saveSettingButton.value){
    saveSettingButton.value.onclick= function(){
      axios
    .post('api/saveSettings', {
      showTrackDelay:  settingsStore.showTrackDelay,
      volumeThreshold:  settingsStore.volumeThreshold ,
      sdRed: settingsStore.sdRed,
      sdGreen: settingsStore.sdGreen,
      sdBlue: settingsStore.sdBlue,
      faderRed: settingsStore.faderRed,
      faderGreen: settingsStore.faderGreen,
      faderBlue: settingsStore.faderBlue

    })
    .catch(function (error) {
      // handle error
      console.log(error)
    })
    }
  }
/* 
  if (volSlider.value) {
    volSlider.value.oninput = function () {
      if (volSlider.value) {
        settingsStore.volumeSliderValue = parseInt(volSlider.value.value)
      }
    }
  }
  if (songDataRed.value) {
    songDataRed.value.oninput = function () {
      if (songDataRed.value) {
        settingsStore.sdRed = parseInt(songDataRed.value.value)
      }
    }
  }
  if (songDataGreen.value) {
    songDataGreen.value.oninput = function () {
      if (songDataGreen.value) {
        settingsStore.sdGreen = parseInt(songDataGreen.value.value)
      }
    }
  }
  if (songDataBlue.value) {
    songDataBlue.value.oninput = function () {
      if (songDataBlue.value) {
        settingsStore.sdBlue = parseInt(songDataBlue.value.value)
      }
    }
  } */
})
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
  <div></div>
  <div class="parent">
    <div class="box">
      <label for="volumeSlider">Volume slider tester</label>
      <input ref="volSliderRef"  v-model="settingsStore.volumeSliderValue" type="range" min="0" max="100" class="slider" id="volumeSlider" />
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
          <input ref="sdRed"   v-model="settingsStore.sdRed" type="range" min="0" max="255" class="slider" id="sdred" />
        </p>
        <p>
          <label for="sdgreen">Green</label>
          <input ref="sdGreen"  v-model="settingsStore.sdGreen" type="range" min="0" max="255" class="slider" id="sdgreen" />
        </p>
        <p>
          <label for="sdblue">Blue</label>
          <input ref="sdBlue"   v-model="settingsStore.sdBlue" type="range" min="0" max="255" class="slider" id="sdblue" />
        </p>
      </div>
      <div>
        <p>Song data transition settings</p>
        <p>
          <label for="animDuration"></label>
          <input ref="transitionDuration" type="float" min="0" max="5" id="animDuration" />
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
input {
  padding: 5px;
}
label {
  padding: 5px;
}
</style>
