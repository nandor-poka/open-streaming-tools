import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { PlaylistStore } from '@/stores/PlaylistStore'

describe('PlaylistStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })
})
