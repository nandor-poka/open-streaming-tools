<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import Navbar from './Navbar.vue'
import { UnitStore } from '@/stores/UnitStore'
import { SettingsStore } from '@/stores/SettingsStore'
import { ChatStore } from '@/stores/ChatStore'
import TwitchClient from './TwitchClient.vue'
import TwitchChatDisplay from './TwitchChatDisplay.vue'
import type { Axios } from 'axios'
import { inject, onMounted, ref, onBeforeUnmount } from 'vue'
const unitStore = UnitStore()

const settingsStore = SettingsStore()
const chatStore = ChatStore()
const axios: Axios = inject('axios') as Axios

const showCredentialsModal = ref(false)
const showConnectionModal = ref(false)

async function connectBot(){
  try {
    const response = await axios.get('/api/twitchBotOAuthUrl')
    const oauthUrl = response.data
    const win = window.open(oauthUrl, '_blank', 'noopener,noreferrer')
    if (win) { try { win.opener = null } catch (e) { /* ignore */ } }
  } catch (error) {
    console.error('Failed to get bot OAuth URL:', error)
  }
}

async function connectBroadcaster(){
  try {
    const response = await axios.get('/api/twitchBroadcasterOAuthUrl')
    const oauthUrl = response.data
    const win = window.open(oauthUrl, '_blank', 'noopener,noreferrer')
    if (win) { try { win.opener = null } catch (e) { /* ignore */ } }
  } catch (error) {
    console.error('Failed to get broadcaster OAuth URL:', error)
  }
}

function closeModals(){
  showCredentialsModal.value = false
  showConnectionModal.value = false
}

function handleKeyDown(e: KeyboardEvent){
  if(e.key === 'Escape'){
    closeModals()
  }
}

onMounted(() => {
  window.addEventListener('keydown', handleKeyDown)
  axios
    .get('api/getSettings', {
      method: 'get',
      headers: {
        'Content-Type': 'application/json',
      },
    })
    .then(function (response) {
      const settings = response.data
      settingsStore.showTrackDelay = settings.showTrackDelay
      settingsStore.volumeThreshold = settings.volumeThreshold
      settingsStore.sdRed = settings.sdRed
      settingsStore.sdGreen = settings.sdGreen
      settingsStore.sdBlue = settings.sdBlue
      settingsStore.faderRed = settings.faderRed
      settingsStore.faderGreen = settings.faderGreen
      settingsStore.faderBlue = settings.faderBlue
      settingsStore.channelUserName = settings.channelUserName
      settingsStore.botUserName = settings.botUserName
      settingsStore.clientIdFilePath = settings.clientIdFilePath
      settingsStore.clientSecretFilePath = settings.clientSecretFilePath
      settingsStore.twitchStatus = settings.twitchStatus
      settingsStore.versionString = settings.versionString
    })
    .catch(function (error) {
      // handle error
      console.log(error)
    })
})

onBeforeUnmount(() => {
  window.removeEventListener('keydown', handleKeyDown)
})

</script>

<template>
  <TwitchClient />
  <Navbar />

  <main class="dashboard">
    <header class="dashboard-header">
      <h1>Dashboard</h1>
      <div class="header-actions">
        <button class="btn btn-primary" @click="connectBot" aria-label="Connect Bot User">
          <!-- robot icon -->
          <svg class="btn-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false"><path d="M12 2a2 2 0 0 0-2 2v1H8a2 2 0 0 0-2 2v3h12V7a2 2 0 0 0-2-2h-2V4a2 2 0 0 0-2-2zM6 14v4a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2v-4H6zm3 2a1 1 0 1 1 0-2 1 1 0 0 1 0 2zm6 0a1 1 0 1 1 0-2 1 1 0 0 1 0 2z"/></svg>
          Connect Bot User
        </button>
        <button class="btn btn-secondary" @click="connectBroadcaster" aria-label="Connect Broadcaster">
          <!-- broadcast icon -->
          <svg class="btn-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false"><path d="M12 3a9 9 0 0 0-9 9 9 9 0 0 0 1 4.2L2 20l3.8-2A9 9 0 1 0 12 3zm0 4a5 5 0 0 1 0 10 5 5 0 0 1 0-10z"/></svg>
          Connect Broadcaster
        </button>
      </div>
    </header>

    <section class="cards">
      <article class="card cred-card">
        <h3>Credentials</h3>
        <ul class="checks">
          <li :class="{ missing: !settingsStore.channelUserName }"><span class="label">Broadcaster:</span> <strong>{{ settingsStore.channelUserName || 'Not configured' }}</strong></li>
          <li :class="{ missing: !settingsStore.botUserName }"><span class="label">Bot:</span> <strong>{{ settingsStore.botUserName || 'Not configured' }}</strong></li>
          <li :class="{ missing: !settingsStore.clientIdFilePath }"><span class="label">Client ID:</span> <strong>{{ settingsStore.clientIdFilePath || 'Not configured' }}</strong></li>
          <li :class="{ missing: !settingsStore.clientSecretFilePath }"><span class="label">Secret:</span> <strong>{{ settingsStore.clientSecretFilePath || 'Not configured' }}</strong></li>
        </ul>
        <button class="card-link" @click="showCredentialsModal = true">View Details →</button>
      </article>

      <article class="card conn-card">
        <h3>Twitch Connection</h3>
        <p><span class="label">Status:</span> <span :class="{'ok': settingsStore.twitchStatus, 'warn': !settingsStore.twitchStatus}">{{ settingsStore.twitchStatus ? 'Connected' : 'Disconnected' }}</span></p>
        <p><span class="label">Response:</span> <em>{{ settingsStore.twitchResponse }}</em></p>
        <button class="card-link" @click="showConnectionModal = true">View Details →</button>
      </article>

      <article class="card device-card">
        <h3>Current Device / Software</h3>
        <p class="muted">{{ unitStore.currentUnit.longName || 'Unknown' }}</p>
        <p class="muted">Version {{ unitStore.currentUnit.version || 'N/A' }}</p>
      </article>

      <div class="chat-container">
        <TwitchChatDisplay />
      </div>
    </section>

    <!-- Credentials Modal -->
    <div v-if="showCredentialsModal" class="modal-overlay" @click="showCredentialsModal = false">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h2>Credentials Details</h2>
          <button class="modal-close" @click="showCredentialsModal = false" aria-label="Close">✕</button>
        </div>
        <div class="modal-body">
          <div class="detail-item">
            <span class="detail-label">Broadcaster Username:</span>
            <span class="detail-value" :class="{ error: !settingsStore.channelUserName }">{{ settingsStore.channelUserName || 'Not configured' }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">Bot Username:</span>
            <span class="detail-value" :class="{ error: !settingsStore.botUserName }">{{ settingsStore.botUserName || 'Not configured' }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">Client ID File Path:</span>
            <span class="detail-value" :class="{ error: !settingsStore.clientIdFilePath }">{{ settingsStore.clientIdFilePath || 'Not configured' }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">Client Secret File Path:</span>
            <span class="detail-value" :class="{ error: !settingsStore.clientSecretFilePath }">{{ settingsStore.clientSecretFilePath || 'Not configured' }}</span>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-primary" @click="showCredentialsModal = false">Close</button>
        </div>
      </div>
    </div>

    <!-- Connection Modal -->
    <div v-if="showConnectionModal" class="modal-overlay" @click="showConnectionModal = false">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h2>Connection Details</h2>
          <button class="modal-close" @click="showConnectionModal = false" aria-label="Close">✕</button>
        </div>
        <div class="modal-body">
          <div class="detail-item">
            <span class="detail-label">Twitch Status:</span>
            <span :class="{'ok': settingsStore.twitchStatus, 'warn': !settingsStore.twitchStatus}">{{ settingsStore.twitchStatus ? 'Connected' : 'Disconnected' }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">Last Response:</span>
            <em>{{ settingsStore.twitchResponse }}</em>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-primary" @click="showConnectionModal = false">Close</button>
        </div>
      </div>
    </div>
  </main>
</template>

<style scoped>
@keyframes fadeIn {
  from { opacity: 0 }
  to { opacity: 1 }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeOut {
  from { opacity: 1 }
  to { opacity: 0 }
}

@keyframes slideDown {
  from {
    opacity: 1;
    transform: translateY(0);
  }
  to {
    opacity: 0;
    transform: translateY(30px);
  }
}

.dashboard{ padding: 1rem; max-width: 1100px; margin: 0 auto }
.dashboard-header{ display:flex; align-items:center; justify-content:space-between; gap:1rem; margin-bottom:1rem }
.header-actions{ display:flex; gap:0.5rem }

.cards{ display:grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap:1rem }
.chat-container { grid-column: 1 / -1; width: 100% }
.card{ background: #000; border: 3px solid var(--azure); padding:1rem; border-radius:8px; box-shadow: 0 2px 8px rgba(0,152,255,0.2) }
.card h3{ margin:0 0 0.75rem 0; color: var(--sgbus-green); border-bottom: 2px solid var(--azure); padding-bottom:0.5rem }

.checks{ list-style:none; padding:0; margin:0 }
.checks li{ padding:0.5rem 0; color: var(--sgbus-green); display:flex; justify-content:space-between; align-items:center; font-weight:500 }
.checks li.missing{ color: #ff6b6b; font-weight:600 }
.checks li.missing .label{ color: #ff6b6b }
.label{ font-weight:600; color: var(--sgbus-green) }

.card-link{ background:transparent; border:none; color: var(--azure); cursor:pointer; font-weight:600; margin-top:0.5rem; padding:0; text-decoration:underline; transition: color 0.2s }
.card-link:hover{ color: var(--sgbus-green) }

.btn{ padding:0.5rem 0.9rem; border-radius:6px; border:1px solid var(--azure); cursor:pointer; font-weight:600; display:inline-flex; align-items:center }
.btn-icon{ width:1rem; height:1rem; margin-right:0.5rem; fill:currentColor }
.btn-primary{ background: var(--azure); color: white; border-color: var(--azure) }
.btn-secondary{ background: transparent; color: var(--sgbus-green); border-color: var(--sgbus-green) }

.muted{ color: #aaa }
.ok{ color: var(--sgbus-green); font-weight:700 }
.warn{ color: #ff6b6b; font-weight:700 }

.conn-card{ border-color: var(--sgbus-green) }
.conn-card h3{ border-bottom-color: var(--sgbus-green) }

/* Modal styles with animations */
.modal-overlay{
  position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.7); display:flex; align-items:center; justify-content:center; z-index:1000;
  animation: fadeIn 0.2s ease-out;
}

.modal{
  background:#000; border: 3px solid var(--azure); border-radius:8px; max-width:500px; width:90%; box-shadow: 0 4px 16px rgba(0,152,255,0.3);
  animation: slideUp 0.3s ease-out;
}

.modal-header{ display:flex; align-items:center; justify-content:space-between; padding:1rem; border-bottom: 2px solid var(--sgbus-green) }
.modal-header h2{ margin:0; color: var(--sgbus-green) }
.modal-close{ background:transparent; border:none; font-size:1.5rem; cursor:pointer; color: var(--azure); transition: color 0.2s }
.modal-close:hover{ color: var(--sgbus-green) }
.modal-body{ padding:1rem }
.modal-footer{ display:flex; gap:0.5rem; justify-content:flex-end; padding:1rem; border-top: 1px solid #333 }

.detail-item{ display:flex; flex-direction:column; margin-bottom:1rem; padding-bottom:0.75rem; border-bottom: 1px solid #333 }
.detail-label{ font-weight:600; color: var(--azure); margin-bottom:0.25rem }
.detail-value{ color: var(--sgbus-green); word-break:break-word }
.detail-value.error{ color: #ff6b6b }

</style>
