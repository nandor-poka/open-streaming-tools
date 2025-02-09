import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { DeckState } from '../types/DeckState'
export const stageLinQStore = defineStore('StageLinQSTore', () => {
  const deck1: DeckState = {
    deckNum: 1,
    faderPos: 0,
    artistName: 'Artist1',
    trackTitle: 'TestTrack1',
    tempo: 174,
  }
  const deck2: DeckState = {
    deckNum: 2,
    faderPos: 0,
    artistName: 'Artist2',
    trackTitle: 'TestTrack2',
    tempo: 174,
  }
  const deck3: DeckState = {
    deckNum: 3,
    faderPos: 0,
    artistName: 'Artist3',
    trackTitle: 'TestTrack3',
    tempo: 175,
  }
  const deck4: DeckState = {
    deckNum: 4,
    faderPos: 0,
    artistName: 'Artist4',
    trackTitle: 'TestTrack4',
    tempo: 176,
  }
  const stagelinQmessages = ref(Array<string>())
  const deck1State = ref(deck1)
  const deck2State = ref(deck2)
  const deck3State = ref(deck3)
  const deck4State = ref(deck4)
  function updateState(newDeckState: DeckState) {
    switch (newDeckState.deckNum) {
      case 1:
        deck1State.value = newDeckState
        break
      case 2:
        deck2State.value = newDeckState
        break
      case 3:
        deck3State.value = newDeckState
        break
      case 4:
        deck4State.value = newDeckState
        break
    }
  }

  return {
    stagelinQmessages,
    deck1State,
    deck2State,
    deck3State,
    deck4State,
    updateState,
  }
})
