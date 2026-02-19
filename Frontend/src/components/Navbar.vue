<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import { SettingsStore } from '@/stores/SettingsStore'
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const settingsStore = SettingsStore()
const router = useRouter()
const route = useRoute()

type NavTab = { id: string; label: string; path: string }
const tabs: NavTab[] = [
  { id: 'dashboard', label: 'Dashboard', path: '/' },
  { id: 'playlists', label: 'Playlists', path: '/playlists' },
  { id: 'log', label: 'Log', path: '/log' },
  { id: 'settings', label: 'Settings', path: '/settings' },
  { id: 'chatbot', label: 'Chatbot', path: '/chatbot' },
  { id: 'browsersource', label: 'BrowserSource', path: '/browsersource' },
]

const tabRefs = ref<Array<any | null>>([])
let onKeyDown: ((e: KeyboardEvent)=>void) | null = null

function isActive(path: string){
  return route.path === path || route.path.startsWith(path + '/')
}

function navigateTo(path: string){
  if(route.path !== path) router.push(path)
}

function focusTabIndex(i: number){
  const btn = tabRefs.value[i]
  if(btn) btn.focus()
}

onMounted(()=>{
  onKeyDown = (e: KeyboardEvent) => {
    const idx = tabRefs.value.findIndex(b => b === document.activeElement)
    if(idx === -1) return
    if(e.key === 'ArrowRight'){
      e.preventDefault()
      const next = (idx + 1) % tabRefs.value.length
      focusTabIndex(next)
      navigateTo(tabs[next].path)
    } else if(e.key === 'ArrowLeft'){
      e.preventDefault()
      const prev = (idx -1 + tabRefs.value.length) % tabRefs.value.length
      focusTabIndex(prev)
      navigateTo(tabs[prev].path)
    } else if(e.key === 'Home'){
      e.preventDefault(); focusTabIndex(0); navigateTo(tabs[0].path)
    } else if(e.key === 'End'){
      e.preventDefault(); focusTabIndex(tabRefs.value.length-1); navigateTo(tabs[tabRefs.value.length-1].path)
    }
  }
  window.addEventListener('keydown', onKeyDown)
})
onBeforeUnmount(()=>{ if(onKeyDown) window.removeEventListener('keydown', onKeyDown) })
</script>

<template>
  <header class="nav-header">
    <h1 class="app-title">Open Streaming Tools {{ settingsStore.versionString }}</h1>
    <nav class="settings-tabs nav-tabs" role="tablist" aria-label="Main navigation">
      <button
        v-for="(tab, i) in tabs"
        :key="tab.id"
        :class="['tab-btn', { active: isActive(tab.path) }]"
        role="tab"
        :aria-selected="isActive(tab.path)"
        @click="navigateTo(tab.path)"
        :ref="el => tabRefs[i] = el"
      >
        {{ tab.label }}
      </button>
    </nav>
  </header>
</template>

<style scoped>
.nav-header{ display:flex; align-items:center; gap:1rem; justify-content:space-between }
.app-title{ font-size:1.2rem; margin:0 }

/* reuse Settings tab styles (kept local to Navbar) */
.nav-tabs{ display:flex; gap:0.5rem; }
.tab-btn{ padding:0.45rem 0.9rem; border-radius:6px; border:1px solid var(--azure); background:transparent; color:var(--azure); cursor:pointer; font-weight:600 }
.tab-btn:hover{ background: rgba(0,152,255,0.06) }
.tab-btn:focus{ outline:3px solid rgba(0,152,255,0.12); outline-offset:2px }
.tab-btn.active{ background:var(--sgbus-green); color:#001800; border-color:var(--sgbus-green); box-shadow: 0 0 0 3px rgba(82,223,0,0.08) inset }
</style>
