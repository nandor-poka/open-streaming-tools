import { createMemoryHistory, createRouter } from 'vue-router'
import Dashboard from './components/Dashboard.vue'
import StageLinQLog from './components/StageLinQLog.vue'

const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: Dashboard,
  },
  {
    path: '/stagelinqlog',
    name: 'log',
    component: StageLinQLog,
  },
]

const router = createRouter({
  history: createMemoryHistory(),
  routes,
})

export default router
