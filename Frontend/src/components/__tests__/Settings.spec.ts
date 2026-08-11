import { mount } from '@vue/test-utils'
import { describe, it, expect, beforeEach, vi } from 'vitest'
import Settings from '@/components/Settings.vue'
import { createPinia, setActivePinia } from 'pinia'
import { SettingsStore } from '@/stores/SettingsStore'

describe('Settings.vue', () => {
  let settingsStore: any

  beforeEach(() => {
    setActivePinia(createPinia())
    settingsStore = SettingsStore()
    settingsStore.channelUserName = 'TestUser'
    settingsStore.botUserName = 'TestBot'
  })

  it('renders settings page with title', () => {
    const wrapper = mount(Settings, {
      global: {
        stubs: { Navbar: true, SettingsGeneral: true, SettingsTwitch: true, SettingsPlayback: true, SettingsDisplay: true },
        provide: { axios: { post: vi.fn() } },
      },
    })
    expect(wrapper.text()).toContain('Settings')
  })

  it('renders tab buttons for all sections', () => {
    const wrapper = mount(Settings, {
      global: {
        stubs: { Navbar: true, SettingsGeneral: true, SettingsTwitch: true, SettingsPlayback: true, SettingsDisplay: true },
        provide: { axios: { post: vi.fn() } },
      },
    })
    expect(wrapper.text()).toContain('General')
    expect(wrapper.text()).toContain('Twitch')
    expect(wrapper.text()).toContain('Playback')
    expect(wrapper.text()).toContain('Display')
  })

  it('marks first tab as active by default', () => {
    const wrapper = mount(Settings, {
      global: {
        stubs: { Navbar: true, SettingsGeneral: true, SettingsTwitch: true, SettingsPlayback: true, SettingsDisplay: true },
        provide: { axios: { post: vi.fn() } },
      },
    })
    const buttons = wrapper.findAll('.tab-btn')
    expect(buttons[0].classes()).toContain('active')
  })

  it('switches tab on button click', async () => {
    const wrapper = mount(Settings, {
      global: {
        stubs: { Navbar: true, SettingsGeneral: true, SettingsTwitch: true, SettingsPlayback: true, SettingsDisplay: true },
        provide: { axios: { post: vi.fn() } },
      },
    })
    const buttons = wrapper.findAll('.tab-btn')
    const twitchBtn = buttons[1]
    await twitchBtn.trigger('click')

    expect(buttons[1].classes()).toContain('active')
  })

  it('renders tab buttons with correct ARIA attributes', () => {
    const wrapper = mount(Settings, {
      global: {
        stubs: { Navbar: true, SettingsGeneral: true, SettingsTwitch: true, SettingsPlayback: true, SettingsDisplay: true },
        provide: { axios: { post: vi.fn() } },
      },
    })
    const tablist = wrapper.find('[role="tablist"]')
    expect(tablist.exists()).toBe(true)

    const buttons = wrapper.findAll('[role="tab"]')
    expect(buttons.length).toBe(4)
    expect(buttons[0].attributes('aria-selected')).toBe('true')
    expect(buttons[1].attributes('aria-selected')).toBe('false')
  })

  it('renders horizontal separator', () => {
    const wrapper = mount(Settings, {
      global: {
        stubs: { Navbar: true, SettingsGeneral: true, SettingsTwitch: true, SettingsPlayback: true, SettingsDisplay: true },
        provide: { axios: { post: vi.fn() } },
      },
    })
    const hrs = wrapper.findAll('hr')
    expect(hrs.length).toBeGreaterThan(0)
  })

  it('has correct tab styling classes', () => {
    const wrapper = mount(Settings, {
      global: {
        stubs: { Navbar: true, SettingsGeneral: true, SettingsTwitch: true, SettingsPlayback: true, SettingsDisplay: true },
        provide: { axios: { post: vi.fn() } },
      },
    })
    const tabbar = wrapper.find('.settings-tabs')
    expect(tabbar.exists()).toBe(true)
    expect(tabbar.classes()).toContain('settings-tabs')
  })
})
