<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import Navbar from './Navbar.vue'
import { UnitStore } from '@/stores/UnitStore'
import { SettingsStore } from '@/stores/SettingsStore'
import { onMounted } from 'vue'
import { inject } from 'vue'
import type { Axios } from 'axios'
const axios: Axios = inject('axios') as Axios
const unitStore = UnitStore()
const settingsStore = SettingsStore()

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
    })
    .catch(function (error) {
      // handle error
      console.log(error)
    })
})
</script>

<template>
  <Navbar />
  <div class="greetings">
    <h1>Dashboard</h1>
  </div>
  <div>
    <h2>Current Device / Software</h2>
    <h3>{{ unitStore.currentUnit.longName }} version {{ unitStore.currentUnit.version }}</h3>
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

.box {
  width: 300px;
  height: 150px;
  border: 1px solid black;
  padding: 10px;
  margin: 10px;
}
.right-aligned {
  margin-left: auto;
  margin-right: 0;
}
.parent {
  display: flex;
}
</style>
