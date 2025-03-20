<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import { PlaylistStore } from '@/stores/PlaylistStore'
import Navbar from './Navbar.vue'
import type { Axios } from 'axios'
import { inject, onMounted, useTemplateRef } from 'vue'
import { TrackStore } from '@/stores/TrackStore'
const axios: Axios = inject('axios') as Axios
const playlistStore = PlaylistStore()
const trackStore = TrackStore()
const playlistSelector = useTemplateRef('playlistSelector')
onMounted(() => {
  axios
    .get('api/getPlaylists', {
      method: 'get',
      headers: {
        'Content-Type': 'application/json',
      },
    })
    .then(function (response) {
      const playlits = response.data
      playlistStore.playlists = playlits
    })
    .catch(function (error) {
      // handle error
      console.log(error)
    })
  if (playlistSelector.value) {
    playlistSelector.value.oninput = function () {
      if (playlistSelector.value)
        axios
          .get('api/getTracksForPlaylist/' + playlistSelector.value.value, {
            method: 'get',
            headers: {
              'Content-Type': 'application/json',
            },
          })
          .then(function (response) {
            const tracks = response.data
            trackStore.tracks = tracks
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
  <h1>Playlists</h1>
  <div>
    <label for="playlistSelector">Available playlists:</label>
    <select ref="playlistSelector" name="playlistSelector" id="playlistSelector">
      <option disabled value="">Select playlist</option>
      <option
        v-for="playlist in playlistStore.playlists"
        :key="playlist.id"
        v-bind:value="playlist.id"
      >
        {{ playlist.title }}
      </option>
    </select>
  </div>
  <div>
    <table>
      <tbody>
        <tr>
          <th>Title</th>
          <th>Artist</th>
          <th>Key</th>
        </tr>
        <tr v-for="track in trackStore.tracks" :key="track.title">
          <td>{{ track.title }}</td>
          <td>{{ track.artist }}</td>
          <td>{{ track.key }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>