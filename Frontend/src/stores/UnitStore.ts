import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { SongData } from '@/types/SongData'
import type { ChannelVolumeData } from '@/types/ChannelVolumeData'
import type { Unit } from '@/types/Unit'
import UnitType from '@/types/UnitType'
export const UnitStore = defineStore('StageLinQSTore', () => {
  const deck1SD: SongData = {
    deckNum: 1,
    artistName: '',
    trackTitle: '',
  }
  const deck2SD: SongData = {
    deckNum: 2,
    artistName: '',
    trackTitle: '',
  }
  const deck3SD: SongData = {
    deckNum: 3,
    artistName: '',
    trackTitle: '',
  }
  const deck4SD: SongData = {
    deckNum: 4,
    artistName: '',
    trackTitle: '',
  }

  const deck1VD: ChannelVolumeData = {
    deckNum: 1,
    volume: 0,
  }
  const deck2VD: ChannelVolumeData = {
    deckNum: 2,
    volume: 0,
  }
  const deck3VD: ChannelVolumeData = {
    deckNum: 3,
    volume: 0,
  }
  const deck4VD: ChannelVolumeData = {
    deckNum: 4,
    volume: 0,
  }
  const unit: Unit = {
    type: UnitType.OTHER,
    deckCount: 0,
    longName: 'None',
    version: '0',
  }
  const currentUnit = ref(unit)
  const stagelinQmessages = ref(Array<string>())
  const deck1SongData = ref(deck1SD)
  const deck2SongData = ref(deck2SD)
  const deck3SongData = ref(deck3SD)
  const deck4SongData = ref(deck4SD)
  const deck1VolumeData = ref(deck1VD)
  const deck2VolumeData = ref(deck2VD)
  const deck3VolumeData = ref(deck3VD)
  const deck4VolumeData = ref(deck4VD)
  function updateSongData(songData: SongData) {
    switch (songData.deckNum) {
      case 1:
        deck1SongData.value = songData

        break
      case 2:
        deck2SongData.value = songData

        break
      case 3:
        deck3SongData.value = songData

        break
      case 4:
        deck4SongData.value = songData
        break
    }
  }
  function updateVolumeData(volumeData: ChannelVolumeData) {
    switch (volumeData.deckNum) {
      case 1:
        deck1VolumeData.value = volumeData
        break
      case 2:
        deck2VolumeData.value = volumeData
        break
      case 3:
        deck3VolumeData.value = volumeData
        break
      case 4:
        deck4VolumeData.value = volumeData
        break
    }
  }
  function updateUnit(newUnit: Unit) {
    currentUnit.value = newUnit
  }
  return {
    stagelinQmessages,
    deck1SongData,
    deck2SongData,
    deck3SongData,
    deck4SongData,
    deck1VolumeData,
    deck2VolumeData,
    deck3VolumeData,
    deck4VolumeData,
    currentUnit,
    updateUnit,
    updateSongData,
    updateVolumeData,
  }
})
