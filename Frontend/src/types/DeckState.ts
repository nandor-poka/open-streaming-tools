export type DeckState = {
  /**
   * Which deck is the info for
   */
  deckNum: number
  /**
   * Position of volume fader
   */
  faderPos: number
  /**
   * Current track's title
   */
  trackTitle: string
  /**
   * Current track's artist
   */
  artistName: string
  /***
   * current bpm
   */
  tempo: number
}
