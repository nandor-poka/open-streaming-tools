<script setup lang="ts">
import { SettingsStore } from '@/stores/SettingsStore'
import { computed } from 'vue'
const settingsStore = SettingsStore()

function toHex(n: number){
  const v = Math.max(0, Math.min(255, Math.round(n)))
  return v.toString(16).padStart(2,'0')
}
function rgbToHex(r:number,g:number,b:number){
  return '#'+ toHex(r)+toHex(g)+toHex(b)
}
function hexToRgb(hex: string){
  const h = hex.replace('#','')
  if(h.length !== 6) return null
  const r = parseInt(h.substring(0,2),16)
  const g = parseInt(h.substring(2,4),16)
  const b = parseInt(h.substring(4,6),16)
  return { r,g,b }
}

const sdColor = computed({
  get(){
    return rgbToHex(settingsStore.sdRed, settingsStore.sdGreen, settingsStore.sdBlue)
  },
  set(hex: string){
    const rgb = hexToRgb(hex)
    if(rgb){
      settingsStore.sdRed = rgb.r
      settingsStore.sdGreen = rgb.g
      settingsStore.sdBlue = rgb.b
    }
  }
})

const faderColor = computed({
  get(){
    return rgbToHex(settingsStore.faderRed, settingsStore.faderGreen, settingsStore.faderBlue)
  },
  set(hex: string){
    const rgb = hexToRgb(hex)
    if(rgb){
      settingsStore.faderRed = rgb.r
      settingsStore.faderGreen = rgb.g
      settingsStore.faderBlue = rgb.b
    }
  }
})
</script>

<template>
  <div class="settings-display dark">
    <div class="header-row">
      <h2>Display / Colors & Animation</h2>
      <div class="header-actions">
        <!-- Dark theme is permanent -->
      </div>
    </div>
    <hr />

    <div class="parent dark">
      <div class="box left-col">
        <div class="control-group">
          <label for="volumeSlider">Volume slider tester</label>
          <input
            ref="volSliderRef"
            v-model="settingsStore.volumeSliderValue"
            type="range"
            min="0"
            max="100"
            class="slider"
            id="volumeSlider"
          />
        </div>

        <div class="picker-row">
          <div class="picker-box">
            <label for="faderColor">Fader level indicator color</label>
            <div class="picker-controls">
              <input id="faderColor" type="color" v-model="faderColor" aria-label="Fader color picker" />
              <span class="hex">{{ faderColor }}</span>
            </div>
          </div>

          <div class="picker-box">
            <label for="sdColor">Song data color</label>
            <div class="picker-controls">
              <input id="sdColor" type="color" v-model="sdColor" aria-label="Song data color picker" />
              <span class="hex">{{ sdColor }}</span>
            </div>
          </div>
        </div>

        <div class="control-group">
          <label for="animDuration">Song data transition duration (s)</label>
          <input ref="transitionDuration" type="number" step="0.1" min="0" max="5" id="animDuration" />
        </div>

        <div class="control-group">
          <label for="animDisposition">Disposition (px)</label>
          <input
            ref="animationDisposition"
            type="number"
            min="50"
            max="250"
            value="50"
            id="animDisposition"
          />
        </div>

        <div class="actions-row">
          <button class="btn" type="button">Test animation</button>
        </div>
      </div>

      <div class="right-col">
        <p>Sample song id display</p>
        <div
          v-bind:style="{
            backgroundImage:
              'linear-gradient(to right, rgb( ' +
                settingsStore.faderRed +',' +
                settingsStore.faderGreen +',' +
                settingsStore.faderBlue +') '  +
              settingsStore.volumeSliderValue +
              '% , var(--ost-deck-empty-color) ' +
              settingsStore.volumeSliderValue +
              '%)',
            color:
              'rgb( ' +
              settingsStore.sdRed +
              ',' +
              settingsStore.sdGreen +
              ',' +
              settingsStore.sdBlue +
              ') ',
          }"
          class="box sample-box"
        >
          <p class="deckNumber">Deck 1</p>

          <p class="songTitle textWrap">
            <b> Track title </b>
          </p>

          <p class="artist">
            <b> Artist</b>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="css">
/* Themeable styling: default light, .dark on root flips colors */
.settings-display{ width:100% }
.settings-display .header-row{ display:flex; align-items:center; justify-content:space-between; gap:1rem }

/* Layout for the settings display: columns with responsive wrapping */
.parent{
  display:flex;
  gap:1rem;
  flex-wrap:wrap;
  width:100%; /* ensure parent spans available width */
  box-sizing: border-box; /* include padding/borders in width calculations */
  align-items: stretch; /* ensure children stretch vertically so parent contains them */
  align-content: flex-start; /* keep wrapped rows packed at the top */
}
/* Make each direct child adapt but keep a sensible minimum width */
.parent > div{
  flex: 1 1 320px;
  min-width: 260px;
  box-sizing: border-box; /* ensure padding doesn't escape */
}

/* Ensure all descendants use border-box so their padding doesn't expand parent unexpectedly */
.parent *, .parent *::before, .parent *::after { box-sizing: inherit }

/* Card style */
.box{
  padding:1rem;
  border-radius:8px;
  border:1px solid rgba(0,0,0,0.06);
  background: #ffffff;
  box-shadow: 0 1px 2px rgba(0,0,0,0.03);
  color: var(--color-text, #000);
}

/* left / right specific */
.left-col{ display:flex; flex-direction:column; gap:0.75rem }
.right-col{ display:flex; flex-direction:column; gap:0.75rem }

/* Dark theme cards: black background, blue border, green text */
.box{
  background: #000000;
  color: var(--sgbus-green);
}

.picker-row{ display:flex; gap:1rem; flex-wrap:wrap; margin-top:0.25rem }
.picker-box{ display:flex; flex-direction:column; gap:0.25rem; min-width:160px; padding:0 }
.picker-controls{ display:flex; align-items:center; gap:0.5rem }
.picker-controls input[type="color"]{ border:none; padding:0; background:transparent; height:2.2rem; width:2.2rem; cursor:pointer; border-radius:4px }
.picker-box .hex{ font-family:monospace; color:var(--sgbus-green); font-size:0.95rem }
.parent.dark .picker-controls .swatch{ border:1px solid rgba(255,255,255,0.12) }

.control-group label{ display:block; font-weight:600; margin-bottom:0.35rem }
.control-group input[type="number"], .control-group input[type="range"]{ width:100% }

.actions-row{ display:flex; justify-content:flex-end; gap:0.5rem; margin-top:0.5rem }
.btn{ padding:0.4rem 0.8rem; border-radius:6px; border:1px solid var(--azure); background:var(--azure); color:#001f1f; cursor:pointer; font-weight:700; transition: background 180ms ease, color 180ms ease, box-shadow 180ms ease }
.btn:hover{ background: linear-gradient(180deg, var(--azure), var(--sgbus-green)); color:#001800 }
.btn:focus{ outline:3px solid rgba(0,152,255,0.12); outline-offset:2px }

.sample-box{ min-height:120px; display:flex; flex-direction:column; justify-content:center }

/* Responsive: stack vertically on narrow screens */
@media (max-width:800px){
  .parent{ flex-direction:column }
  .parent > div{ flex: 1 1 100%; min-width: auto }
  .picker-row{ flex-direction:column }
  .actions-row{ justify-content:space-between }
}
</style>
