import './assets/main.css'

import { createApp, provide } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'
import axios from 'axios'
const Axios = axios;
Axios.defaults.baseURL = 'http://localhost:8080/'

const app = createApp(App)
app.provide("axios", Axios)
app.use(router)
app.use(createPinia())
app.mount('#app')
