import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'
import Axios from 'axios'
import { SettingsStore } from './stores/SettingsStore'

const axiosInstance = Axios.create()
axiosInstance.defaults.baseURL = 'http://localhost:8080/'
const twitchClient = new WebSocket('wss://eventsub.wss.twitch.tv/ws')
const app = createApp(App)
app.provide('axios', axiosInstance)
app.provide('websocket', twitchClient)
app.use(router)
app.use(createPinia())
const settingsStore = SettingsStore()
app.provide('settingsStore', settingsStore)
app.mount('#app')
