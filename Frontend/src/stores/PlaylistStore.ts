import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { Playlist } from '@/types/Playlist'
export const PlaylistStore = defineStore('PlaylistStore', () => {
  const playlists = ref(Array<Playlist>)
  const activePlaylistId = ref(0)

  return {
    playlists,
    activePlaylistId,
  }
})
