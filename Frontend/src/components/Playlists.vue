<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import { PlaylistStore } from '@/stores/PlaylistStore'
import Navbar from './Navbar.vue'
import type { Axios } from 'axios'
import { inject, onMounted, ref } from 'vue'
import { TrackStore } from '@/stores/TrackStore'
import TrackRow from './playlists/TrackRow.vue'
import TrackMetadataPanel from './playlists/TrackMetadataPanel.vue'
const axios: Axios = inject('axios') as Axios
const playlistStore = PlaylistStore()
const trackStore = TrackStore()
const selectedPlaylistId = ref<number | string | null>(null)

onMounted(() => {
  loadPlaylists()
})

function loadPlaylists(){
  axios
    .get('api/getPlaylists')
    .then(function (response) {
      playlistStore.playlists = response.data
    })
    .catch(function (error) {
      console.log(error)
    })
}

function onPlaylistChange(e: Event){
  const target = e.target as HTMLSelectElement
  const id = target.value
  selectedPlaylistId.value = id
  axios
    .get('api/getTracksForPlaylist/' + id)
    .then(function (response) {
      const tracks = response.data
      // ensure expanded flag
      trackStore.tracks = tracks.map((t:any)=> ({...t, expanded:false}))
    })
    .catch(function (error) {
      console.log(error)
    })
}

function toggleExpand(id: number | string){
  const idx = trackStore.tracks.findIndex((t:any)=>t.id===id || t.title===id)
  if (idx>=0){
    const willExpand = !trackStore.tracks[idx].expanded
    trackStore.tracks[idx].expanded = willExpand
    if (willExpand){
      // fetch metadata if missing
      const track = trackStore.tracks[idx]
      if ((!track.metadata || Object.keys(track.metadata).length===0) && (track.id || track.url)){
        const metaId = track.id ?? track.url
        axios.get('api/getTrackMetadata/' + metaId)
          .then(function(response){
            track.metadata = response.data
          })
          .catch(function(err){
            console.log('metadata fetch error', err)
          })
      }
    }
  }
}
</script>

<template>
  <Navbar />
  <h1>Playlists</h1>
  <div>
    <label for="playlistSelector">Available playlists:</label>
    <select @change="onPlaylistChange" name="playlistSelector" id="playlistSelector">
      <option disabled value="">Select playlist</option>
      <option
        v-for="playlist in playlistStore.playlists"
        :key="playlist.id"
        :value="playlist.id"
      >
        {{ playlist.title }}
      </option>
    </select>
  </div>
  <div v-if="trackStore.tracks.length===0">
    <p>No tracks loaded. Select a playlist.</p>
  </div>
  <div v-else>
    <table>
      <tbody>
        <tr>
          <th>Title</th>
          <th>Artist</th>
          <th>Key</th>
          <th>Duration</th>
        </tr>
        <template v-for="track in trackStore.tracks" :key="track.id ?? track.title">
          <TrackRow :track="track" @toggle-expand="toggleExpand" />
          <tr v-if="track.expanded">
            <td colspan="4">
              <TrackMetadataPanel :metadata="track.metadata" />
            </td>
          </tr>
        </template>
      </tbody>
    </table>
  </div>
</template>
