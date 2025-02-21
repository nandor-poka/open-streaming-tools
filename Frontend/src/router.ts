import { createWebHistory, createRouter } from 'vue-router'
import Dashboard from './components/Dashboard.vue'
import Log from './components/Log.vue'
import BrowserSource from './components/BrowserSource.vue'
import Settings from './components/Settings.vue'
const routes = [
  {
    path: '/',
    component: Dashboard,
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
    path: '/browsersource',
    component: BrowserSource,
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
