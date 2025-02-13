export type DeckState = {
  /**
   * Which deck is the info for
   */
  deckNum: number
  /**
   * Channel volume output
   */
  volume: number
  /**
   * Current track's title
   */
  trackTitle: string
  /**
   * Current track's artist
   */
  artistName: string
}
