package com.openstreamingtools.MainServer.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchWebsocketMessagePayloadSubscriptionCondition {
   private String broadcaster_user_id;
   private String moderator_user_id;
   private String user_id;
   private String from_broadcaster_user_id;
   private String to_broadcaster_user_id;
   private String reward_id;
   private String client_id;
   private String conduit_id;
   private String organization_id;
   private String category_id;
   private String campaign_id;
   private String extension_client_id;

}
