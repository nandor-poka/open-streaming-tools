import StageLinQAction from './StageLinQAction'
import ModelType from './ModelType'
import type { StageLinQMessage } from './StageLinQMessage'

export interface StageLinQDiscoveryMessage extends StageLinQMessage {
  action: StageLinQAction
  modelType: ModelType
  softwareVersion: string
}
