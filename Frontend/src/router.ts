import { createWebHistory, createRouter } from 'vue-router'
import Dashboard from './components/Dashboard.vue'
import StageLinQLog from './components/StageLinQLog.vue'
import BrowserSource from './components/Browsersource.vue'
const routes = [
  {
    path: '/',
    component: Dashboard,
  },
  {
    path: '/stagelinqlog',
    component: StageLinQLog,
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
