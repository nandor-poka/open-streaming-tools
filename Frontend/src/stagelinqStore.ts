import {defineStore} from 'pinia'
import type { StageLinQMessage } from './types/StageLinQMessage'
const stageLinQStore = defineStore('StageLinQSTore'){
    const stagelinQmessages : ref(Array<StageLinQMessage>)
}