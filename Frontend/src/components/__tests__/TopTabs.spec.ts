import { mount } from '@vue/test-utils'
import { describe, it, expect } from 'vitest'
import TopTabs from '@/components/TopTabs.vue'

describe('TopTabs', () => {
  it('is present but renders hidden container when disabled', () => {
    const wrapper = mount(TopTabs)
    const div = wrapper.find('div')
    expect(div.exists()).toBe(true)
    expect(div.attributes('style')).toContain('display')
    expect(div.attributes('style')).toContain('none')
  })
})
