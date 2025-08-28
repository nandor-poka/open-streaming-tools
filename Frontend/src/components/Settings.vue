<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import Navbar from './Navbar.vue'
import { SettingsStore } from '@/stores/SettingsStore'
import type { Axios } from 'axios'
import { inject, onMounted, useTemplateRef } from 'vue'

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
      faderBlue: settingsStore.faderBlue,
      channelUserName: settingsStore.channelUserName,
      botUserName: settingsStore.botUserName,
      clientIdFilePath: settingsStore.clientIdFilePath,
      clientSecretFilePath: settingsStore.clientSecretFilePath

    })
    .catch(function (error) {
      // handle error
      console.log(error)
    })
    }
  }

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
          <td>User name for the Twitch channel to send message to</td>
          <td>
            <input v-model="settingsStore.channelUserName" type="text" id="channelUserName" />
          </td>
          <td>
            The name of the Twitch user who owns the channel that the bot will send message to. Typically your own user name for your own channel.
          </td>
        </tr>
        <tr>
          <td>User name for the Twitch user that acts as the bot</td>
          <td>
            <input v-model="settingsStore.botUserName" type="text" id="botUserName" />
          </td>
          <td>
            The name of the Twitch user who owns the channel that the bot will send message to. Typically your own user name for your own channel.
          </td>
        </tr>
        <tr>
          <td>Path to the file containing the client ID</td>
          <td>
            <input v-model="settingsStore.clientIdFilePath" type="text" id="clientIDPath" />
          </td>
          <td>
            Fully quialified path to the file that has the Twitch client ID.
          </td>
        </tr>
        <tr>
          <td>Path to the file containing the client secret</td>
          <td>
            <input v-model="settingsStore.clientSecretFilePath" type="text" id="clientSecretPath" />
          </td>
          <td>
            Fully quialified path to the file that has the Twitch client secret.
          </td>
        </tr>
        <hr/>

        <tr>
          <td>Show track after info</td>
          <td>
            <input v-model="settingsStore.showTrackDelay" type="number" id="showTrackDelay" />
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
              v-model="settingsStore.volumeThreshold"
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
