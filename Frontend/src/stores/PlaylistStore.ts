import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { Playlist } from '@/types/Playlist'
export const PlaylistStore = defineStore('PlaylistStore', () => {
  const playlists = ref(Array<Playlist>())
  const activePlaylistId = ref(0)
  const playlistDatabasePath = ref('../databases/enginedj.db')
  async function fetchPlaylists(fetcher?: () => Promise<any>) {
    try {
      let data
      if (fetcher) {
        data = await fetcher()
      } else {
        // default: try calling backend endpoint
        const res = await fetch('/api/getPlaylists')
        data = await res.json()
      }
      playlists.value = data
    } catch (e) {
      console.error('fetchPlaylists error', e)
      playlists.value = []
    }
  }
  return {
    playlists,
    activePlaylistId,
    playlistDatabasePath,
    fetchPlaylists,
  }
})
