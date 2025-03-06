<script setup lang="ts">
import { inject, onMounted, useTemplateRef } from 'vue'
import Navbar from './Navbar.vue'
import { SettingsStore } from '@/stores/SettingsStore'
import type { Axios } from 'axios'
const settingsStore = SettingsStore()
const saveSettingButton = useTemplateRef("saveSettings")
const axios = inject("axios") as Axios
onMounted(() => {


  if(saveSettingButton.value){
    saveSettingButton.value.onclick= function(){
      axios
    .post('saveSettings', {
      showTrackDelay:  settingsStore.showTrackDelay,
      volumeThreshold:  settingsStore.volumeThreshold ,
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
    <p>
      <label for="showTrackDelay">Show track after info (seconds, 0 disables delay)</label>
      <input v-model="settingsStore.showTrackDelay" type="number" id="showTrackDelay" />
    </p>
    <p>
      <label for="volumeThreshold"
        >Only show track info if volume is above (percent, 0 disables)</label
      >
      <input v-model="settingsStore.volumeThreshold" type="number" id="volumeThreshold" />
    </p>
  </div>
  <button ref="saveSettings" type="button">Save config</button>
</template>
