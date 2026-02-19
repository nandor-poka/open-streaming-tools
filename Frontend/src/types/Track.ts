export type Track = {
  id?: number | string
  title: string
  artist?: string
  key?: number
  duration?: number // seconds
  url?: string
  metadata?: {
    bitrate?: number
    sampleRate?: number
    sizeBytes?: number
  }
  expanded?: boolean
}
