import { mount } from '@vue/test-utils'
import { describe, it, expect, beforeEach, vi } from 'vitest'
import Dashboard from '@/components/Dashboard.vue'
import { createPinia, setActivePinia } from 'pinia'
import { SettingsStore } from '@/stores/SettingsStore'
import { UnitStore } from '@/stores/UnitStore'

describe('Dashboard.vue', () => {
  let settingsStore: any
  let unitStore: any
  let mockAxios: any

  beforeEach(() => {
    setActivePinia(createPinia())
    settingsStore = SettingsStore()
    unitStore = UnitStore()

    // Mock axios properly
    mockAxios = {
      get: vi.fn().mockResolvedValue({
        data: {
          showTrackDelay: 0,
          volumeThreshold: 0,
          sdRed: 255,
          sdGreen: 0,
          sdBlue: 0,
          faderRed: 0,
          faderGreen: 255,
          faderBlue: 0,
          channelUserName: 'TestBroadcaster',
          botUserName: 'TestBot',
          clientIdFilePath: '/path/to/id',
          clientSecretFilePath: '/path/to/secret',
          twitchStatus: true,
          versionString: 'v1.0',
        },
      }),
    }

    settingsStore.channelUserName = 'TestBroadcaster'
    settingsStore.botUserName = 'TestBot'
    settingsStore.clientIdFilePath = '/path/to/id'
    settingsStore.clientSecretFilePath = '/path/to/secret'
    settingsStore.twitchStatus = true
    settingsStore.twitchResponse = 'OK'

    unitStore.currentUnit.longName = 'Test Device'
    unitStore.currentUnit.version = '1.0.0'
  })

  it('renders dashboard header with title', () => {
    const wrapper = mount(Dashboard, {
      global: {
        stubs: { Navbar: true, TwitchClient: true },
        provide: { axios: mockAxios },
      },
    })
    expect(wrapper.text()).toContain('Dashboard')
  })

  it('renders three cards', () => {
    const wrapper = mount(Dashboard, {
      global: {
        stubs: { Navbar: true, TwitchClient: true },
        provide: { axios: mockAxios },
      },
    })
    const cards = wrapper.findAll('.card')
    expect(cards.length).toBe(3)
  })

  it('renders OAuth buttons', () => {
    const wrapper = mount(Dashboard, {
      global: {
        stubs: { Navbar: true, TwitchClient: true },
        provide: { axios: mockAxios },
      },
    })
    const buttons = wrapper.findAll('.btn')
    expect(buttons.length).toBeGreaterThanOrEqual(2)
  })

  it('opens modal on View Details click', async () => {
    const wrapper = mount(Dashboard, {
      global: {
        stubs: { Navbar: true, TwitchClient: true },
        provide: { axios: mockAxios },
      },
    })
    expect(wrapper.find('.modal-overlay').exists()).toBe(false)
    const link = wrapper.findAll('.card-link')[0]
    await link.trigger('click')
    expect(wrapper.find('.modal-overlay').exists()).toBe(true)
  })

  it('closes modal on overlay click', async () => {
    const wrapper = mount(Dashboard, {
      global: {
        stubs: { Navbar: true, TwitchClient: true },
        provide: { axios: mockAxios },
      },
    })
    const link = wrapper.findAll('.card-link')[0]
    await link.trigger('click')
    expect(wrapper.find('.modal-overlay').exists()).toBe(true)

    const overlay = wrapper.find('.modal-overlay')
    await overlay.trigger('click')
    expect(wrapper.find('.modal-overlay').exists()).toBe(false)
  })

  it('closes modal on close button click', async () => {
    const wrapper = mount(Dashboard, {
      global: {
        stubs: { Navbar: true, TwitchClient: true },
        provide: { axios: mockAxios },
      },
    })
    const link = wrapper.findAll('.card-link')[0]
    await link.trigger('click')
    const closeBtn = wrapper.find('.modal-close')
    await closeBtn.trigger('click')
    expect(wrapper.find('.modal-overlay').exists()).toBe(false)
  })

  it('renders cards with correct CSS classes', () => {
    const wrapper = mount(Dashboard, {
      global: {
        stubs: { Navbar: true, TwitchClient: true },
        provide: { axios: mockAxios },
      },
    })
    const cards = wrapper.findAll('.card')
    expect(cards[0].classes()).toContain('cred-card')
    expect(cards[1].classes()).toContain('conn-card')
    expect(cards[2].classes()).toContain('device-card')
  })

  it('displays Connected status when twitchStatus is true', () => {
    const wrapper = mount(Dashboard, {
      global: {
        stubs: { Navbar: true, TwitchClient: true },
        provide: { axios: mockAxios },
      },
    })
    const statusElements = wrapper.findAll('.ok')
    expect(statusElements.length).toBeGreaterThan(0)
  })

  it('displays modal header with title', async () => {
    const wrapper = mount(Dashboard, {
      global: {
        stubs: { Navbar: true, TwitchClient: true },
        provide: { axios: mockAxios },
      },
    })
    const link = wrapper.findAll('.card-link')[0]
    await link.trigger('click')

    const modalHeader = wrapper.find('.modal-header h2')
    expect(modalHeader.exists()).toBe(true)
  })
})
