import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'
import Axios from 'axios'
const axiosInstance = Axios.create()
axiosInstance.defaults.baseURL = 'http://localhost:8080/'
const app = createApp(App)
app.provide('axios', axiosInstance)
app.use(router)
app.use(createPinia())
app.mount('#app')
