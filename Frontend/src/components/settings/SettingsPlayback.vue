<script setup lang="ts">
import { SettingsStore } from '@/stores/SettingsStore'
const settingsStore = SettingsStore()
</script>

<template>
  <h2>Playback / Conditions</h2>
  <hr />
  <table>
    <tbody>
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
          <input v-model="settingsStore.volumeThreshold" type="number" id="volumeThreshold" />
        </td>
        <td>
          percent, this takes into consideration cross-fader position as well, 0 disables this
          condition, meaning that track info will be shown immedaitely after track loading or any
          other condition fulfilment
        </td>
      </tr>
    </tbody>
  </table>
</template>

