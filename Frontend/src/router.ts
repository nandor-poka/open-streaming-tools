import { createWebHistory, createRouter } from 'vue-router'
import Dashboard from './components/Dashboard.vue'
import Log from './components/Log.vue'
import BrowserSource from './components/BrowserSource.vue'
import Settings from './components/Settings.vue'
import Playlists from './components/Playlists.vue'
import Chatbot from './components/Chatbot.vue'
const routes = [
  {
    path: '/',
    component: Dashboard,
  },
  {
    path: '/playlists',
    component: Playlists,
  },
  {
    path: '/log',
    component: Log,
  },
  {
    path: '/settings',
    component: Settings,
  },
  {
    path: '/chatbot',
    component: Chatbot,
  },
  {
    path: '/browsersource',
    component: BrowserSource,
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
