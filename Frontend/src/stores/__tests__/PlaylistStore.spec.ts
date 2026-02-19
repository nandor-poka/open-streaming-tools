import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { PlaylistStore } from '@/stores/PlaylistStore'

describe('PlaylistStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })
  it('fetches playlists via provided fetcher', async () => {
    const store = PlaylistStore()
    await store.fetchPlaylists(() => Promise.resolve([{ id: 1, title: 'p1' }]))
    expect(store.playlists.length).toBe(1)
    expect(store.playlists[0].title).toBe('p1')
  })
  it('handles fetcher error gracefully', async () => {
    const store = PlaylistStore()
    await store.fetchPlaylists(() => Promise.reject(new Error('fail')))
    expect(store.playlists.length).toBe(0)
  })
})
