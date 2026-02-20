<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import Navbar from './Navbar.vue'
import { SettingsStore } from '@/stores/SettingsStore'
import type { Axios } from 'axios'
import { inject, onMounted, useTemplateRef, ref, onBeforeUnmount } from 'vue'

import SettingsGeneral from './settings/SettingsGeneral.vue'
import SettingsTwitch from './settings/SettingsTwitch.vue'
import SettingsPlayback from './settings/SettingsPlayback.vue'
import SettingsDisplay from './settings/SettingsDisplay.vue'

type TabId = 'general' | 'twitch' | 'playback' | 'display'
const settingsStore = SettingsStore()
const saveSettingButton = useTemplateRef('saveSettings')
const axios = inject('axios') as Axios

const activeTab = ref<TabId>('general')
const tabs: { id: TabId; label: string }[] = [
  { id: 'general', label: 'General' },
  { id: 'twitch', label: 'Twitch' },
  { id: 'playback', label: 'Playback' },
  { id: 'display', label: 'Display' },
]

// refs for keyboard navigation
const tabRefs = ref<Array<HTMLButtonElement | null>>([])
let onKeyDown: ((e: KeyboardEvent) => void) | null = null

function focusTabIndex(i: number){
  const btn = tabRefs.value[i]
  if(btn) btn.focus()
}

function selectTab(id: TabId) { activeTab.value = id }

onMounted(() => {
  if (saveSettingButton.value) {
    saveSettingButton.value.onclick = function () {
      axios
        .post('api/saveSettings', {
          showTrackDelay: settingsStore.showTrackDelay,
          volumeThreshold: settingsStore.volumeThreshold,
          sdRed: settingsStore.sdRed,
          sdGreen: settingsStore.sdGreen,
          sdBlue: settingsStore.sdBlue,
          faderRed: settingsStore.faderRed,
          faderGreen: settingsStore.faderGreen,
          faderBlue: settingsStore.faderBlue,
          channelUserName: settingsStore.channelUserName,
          botUserName: settingsStore.botUserName,
          clientIdFilePath: settingsStore.clientIdFilePath,
          clientSecretFilePath: settingsStore.clientSecretFilePath,
          //autoShoutoutList : settingsStore.autoShoutoutList
        })
        .catch(function (error) {
          // handle error
          console.log(error)
        })
    }
  }

  // keyboard navigation handler on the tablist
  onKeyDown = (e: KeyboardEvent) => {
    const idx = tabRefs.value.findIndex((b)=> b === document.activeElement)
    if(idx === -1) return
    if(e.key === 'ArrowRight'){
      e.preventDefault()
      const next = (idx + 1) % tabRefs.value.length
      focusTabIndex(next)
      activeTab.value = tabs[next].id
    } else if(e.key === 'ArrowLeft'){
      e.preventDefault()
      const prev = (idx - 1 + tabRefs.value.length) % tabRefs.value.length
      focusTabIndex(prev)
      activeTab.value = tabs[prev].id
    } else if(e.key === 'Home'){
      e.preventDefault()
      focusTabIndex(0)
      activeTab.value = tabs[0].id
    } else if(e.key === 'End'){
      e.preventDefault()
      focusTabIndex(tabRefs.value.length-1)
      activeTab.value = tabs[tabRefs.value.length-1].id
    }
  }
  window.addEventListener('keydown', onKeyDown)
})
onBeforeUnmount(()=>{ if(onKeyDown) window.removeEventListener('keydown', onKeyDown) })
</script>

<template>
  <Navbar />

  <!-- Header with title and right-aligned save button -->
  <div class="settings-header">
    <div class="title-actions">
      <h1>Settings</h1>
      <button
        ref="saveSettings"
        class="save-btn in-title"
        aria-label="Save settings"
        title="Save settings"
        type="button"
      >
        Save
      </button>
    </div>
  </div>

  <!-- Tab bar -->
  <div class="settings-tabs" role="tablist" aria-label="Settings sections">
    <button
      v-for="(tab, index) in tabs"
      :key="tab.id"
      :class="['tab-btn', { active: activeTab === tab.id }]"
      @click="selectTab(tab.id)"
      role="tab"
      :aria-selected="activeTab === tab.id"
      :ref="el => tabRefs[index] = el"
    >
      {{ tab.label }}
    </button>
  </div>

  <hr />

  <component :is="activeTab === 'general' ? SettingsGeneral : activeTab === 'twitch' ? SettingsTwitch : activeTab === 'playback' ? SettingsPlayback : SettingsDisplay" />

  <!-- Fallback / empty area if no tab selected -->
  <div v-if="!activeTab" style="padding:1rem">Select a section</div>
</template>

<style lang="css">
table {
  max-width: 75%;
}
td {
  padding: 5px;
}

/* Use project blue/green variables for separators and tabs */
hr {
  border: 1px solid var(--azure);
}
input {
  padding: 5px;
}
label {
  padding: 5px;
}

/* Header */
.settings-header{
  display:flex;
  align-items:center;
  justify-content:space-between;
  gap:1rem;
  margin-bottom:0.5rem;
}
.title-actions{ display:flex; gap:0.5rem; align-items:center }
.save-btn{
  padding:0.45rem 0.9rem;
  border-radius:6px;
  border:1px solid var(--azure);
  background:var(--azure);
  color:#001f1f; /* darkish text */
  cursor:pointer;
  font-weight:700;
  transition: background 180ms ease, color 180ms ease, box-shadow 180ms ease;
}
.save-btn.in-title{ padding:0.25rem 0.6rem; font-size:0.95rem }
.save-btn:hover{ background: linear-gradient(180deg, var(--azure), var(--sgbus-green)); color:#001800 }
.save-btn:focus{ outline:3px solid rgba(0,152,255,0.12); outline-offset:2px }

/* Tabs */
.settings-tabs{
  display:flex;
  gap:0.5rem;
  margin-bottom:0.25rem;
}
.tab-btn{
  padding:0.45rem 0.9rem;
  border-radius:6px;
  border:1px solid var(--azure);
  background:transparent;
  color:var(--azure);
  cursor:pointer;
  font-weight:600;
  transition: background 180ms ease, color 180ms ease, box-shadow 180ms ease, border-color 180ms ease;
}
.tab-btn:hover{
  background: rgba(0,152,255,0.06); /* subtle blue hover */
}
.tab-btn:focus{
  outline:3px solid rgba(0,152,255,0.12);
  outline-offset:2px;
}
.tab-btn.active{
  /* Match the save button gradient for active state */
  background: linear-gradient(180deg, var(--azure), var(--sgbus-green));
  color: #001800; /* dark text on bright green */
  border-color: var(--sgbus-green);
  box-shadow: 0 0 0 3px rgba(82,223,0,0.08) inset;
}

.settings-section{
  color: var(--color-text); /* ensure section text matches project color */
  margin-top: 0.5rem;
}

.parent{ display:flex; gap:1rem; align-items:flex-start }
.box{ padding:1rem; border:1px solid #eee; background:white }
</style>
