import { mount } from '@vue/test-utils'
import { describe, it, expect, beforeEach } from 'vitest'
import Navbar from '@/components/Navbar.vue'
import { createRouter, createWebHistory } from 'vue-router'
import { createPinia, setActivePinia } from 'pinia'

describe('Navbar.vue', () => {
  let router: any

  beforeEach(() => {
    setActivePinia(createPinia())
    const routes = [
      { path: '/', component: { template: '<div>Dashboard</div>' } },
      { path: '/playlists', component: { template: '<div>Playlists</div>' } },
      { path: '/settings', component: { template: '<div>Settings</div>' } },
    ]
    router = createRouter({ history: createWebHistory(), routes })
  })

  it('renders navbar with tab buttons', async () => {
    router.push('/')
    await router.isReady()
    const wrapper = mount(Navbar, {
      global: {
        plugins: [router],
      },
    })
    expect(wrapper.text()).toContain('Dashboard')
    expect(wrapper.text()).toContain('Playlists')
  })

  it('marks active tab with active class', async () => {
    router.push('/')
    await router.isReady()
    const wrapper = mount(Navbar, {
      global: {
        plugins: [router],
      },
    })
    const buttons = wrapper.findAll('.tab-btn')
    expect(buttons[0].classes()).toContain('active')
  })

  it('has correct ARIA attributes', async () => {
    router.push('/')
    await router.isReady()
    const wrapper = mount(Navbar, {
      global: {
        plugins: [router],
      },
    })
    const tablist = wrapper.find('[role="tablist"]')
    expect(tablist.exists()).toBe(true)
    const buttons = wrapper.findAll('[role="tab"]')
    expect(buttons.length).toBeGreaterThan(0)
  })

  it('renders nav with proper structure', async () => {
    router.push('/')
    await router.isReady()
    const wrapper = mount(Navbar, {
      global: {
        plugins: [router],
      },
    })
    const nav = wrapper.find('nav.nav-tabs')
    expect(nav.exists()).toBe(true)
  })
})
