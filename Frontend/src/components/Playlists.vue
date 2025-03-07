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
const dbSelector = useTemplateRef('dbSelector')
const updteDb = useTemplateRef('updateDB')

onMounted(() => {
  if (playlistStore.playlistDatabasePath != '') {
    if (dbSelector.value) {
      if (dbSelector.value.value) {
        axios
          .post(
            'addDataSource',
            {
              path: dbSelector.value.value,
            },
            {
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
              },
            },
          )
          .then(function (response) {
            const result = response.data
            if (result.dbInitialized) {
              if (dbSelector.value) {
                if (dbSelector.value.files) {
                  playlistStore.playlistDatabasePath = dbSelector.value.value
                }
              }

              axios
                .get('getPlaylists', {
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
            }
          })
          .catch(function (error) {
            // handle error
            console.log(error)
          })
      }
    }
  }

  if (updteDb.value) {
    updteDb.value.onclick = function () {
      if (dbSelector.value) {
        if (dbSelector.value.value) {
          axios
            .post(
              'addDataSource',
              {
                path: dbSelector.value.value,
              },
              {
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded',
                },
              },
            )
            .then(function (response) {
              const result = response.data
              if (result.dbInitialized) {
                if (dbSelector.value) {
                  if (dbSelector.value.files) {
                    playlistStore.playlistDatabasePath = dbSelector.value.value
                  }
                }

                axios
                  .get('getPlaylists', {
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
              }
            })
            .catch(function (error) {
              // handle error
              console.log(error)
            })
        }
      }
    }
  }

  if (playlistSelector.value) {
    playlistSelector.value.oninput = function () {
      if (playlistSelector.value)
        axios
          .get('getTracksForPlaylist/' + playlistSelector.value.value, {
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
    <label for="dbSelector">Select database file:</label>
    <input
      type="text"
      ref="dbSelector"
      name="dbSelector"
      id="dbSelector"
      v-bind:value="playlistStore.playlistDatabasePath"
    />
    <input type="button" value="Update Database" ref="updateDB" />
    <label v-if="playlistStore.playlistDatabasePath != ''" for="playlistSelector"
      >Available playlists:</label
    >
    <select
      v-if="playlistStore.playlistDatabasePath != ''"
      ref="playlistSelector"
      name="playlistSelector"
      id="playlistSelector"
    >
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
