import UnitType from './UnitType'
export type Unit = {
  /**
   * Unit type as in Controller, Mixer, Software, Player, or Other for unkown
   */
  type: UnitType
  // ModelType   modelType;
  /**
   * The name of the unit that is canonically used
   */
  longName: string
  /**
   * sotware or hardware version
   */
  version: string

  /**
   * number of available decks
   */
  deckCount: number
}
