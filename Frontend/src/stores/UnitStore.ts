import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { SongData } from '@/types/SongData'
import type { ChannelVolumeData } from '@/types/ChannelVolumeData'
import type { Unit } from '@/types/Unit'
import UnitType from '@/types/UnitType'
export const UnitStore = defineStore('StageLinQSTore', () => {
  const deck1UpdateSwtich = ref(false)
  const deck2UpdateSwtich = ref(false)
  const deck3UpdateSwtich = ref(false)
  const deck4UpdateSwtich = ref(false)
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
        if (deck1SongData.value != songData) {
          if (songData.artistName == ' ' && songData.trackTitle == ' ') {
            deck1UpdateSwtich.value = false
          } else {
            deck1UpdateSwtich.value = true
          }

          deck1SongData.value = songData
        }

        break
      case 2:
        if (deck2SongData.value != songData) {
          if (songData.artistName == ' ' && songData.trackTitle == ' ') {
            deck2UpdateSwtich.value = false
          } else {
            deck2UpdateSwtich.value = true
          }

          deck2SongData.value = songData
        }
        break
      case 3:
        if (deck3SongData.value != songData) {
          if (songData.artistName == ' ' && songData.trackTitle == ' ') {
            deck3UpdateSwtich.value = false
          } else {
            deck3UpdateSwtich.value = true
          }

          deck3SongData.value = songData
        }
        break
      case 4:
        if (deck4SongData.value != songData) {
          if (songData.artistName == ' ' && songData.trackTitle == ' ') {
            deck4UpdateSwtich.value = false
          } else {
            deck4UpdateSwtich.value = true
          }

          deck4SongData.value = songData
        }
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
    deck1UpdateSwtich,
    deck2UpdateSwtich,
    deck3UpdateSwtich,
    deck4UpdateSwtich,
    updateUnit,
    updateSongData,
    updateVolumeData,
  }
})
