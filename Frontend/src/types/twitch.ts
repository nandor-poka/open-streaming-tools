/**
 * Twitch EventSub event types for frontend STOMP subscriptions
 */

export interface TwitchSessionWelcome {
  type: 'session_welcome'
  sessionId: string
  timestamp: number
}

export interface TwitchChatMessage {
  type: 'channel.chat.message'
  event: {
    message?: {
      text: string
    }
    chatter_user_login?: string
    chatter_user_name?: string
  }
  timestamp: number
}

export interface TwitchChatCommand {
  command: 'recommend' | 'shoutout' | string
  event: {
    message?: {
      text: string
    }
    chatter_user_login?: string
    chatter_user_name?: string
  }
  timestamp: number
}

export interface TwitchChannelPointsRedemption {
  type: 'channel.channel_points_redemption'
  event: {
    id?: string
    user_login?: string
    user_name?: string
    user_input?: string
    reward?: {
      id?: string
      title?: string
    }
  }
  timestamp: number
}

export interface TwitchConnectionStatus {
  status: 'connected' | 'disconnected'
  timestamp: number
}

export interface TwitchError {
  error: string
  timestamp: number
}

export type TwitchEvent =
  | TwitchSessionWelcome
  | TwitchChatMessage
  | TwitchChatCommand
  | TwitchChannelPointsRedemption
  | TwitchConnectionStatus
  | TwitchError



