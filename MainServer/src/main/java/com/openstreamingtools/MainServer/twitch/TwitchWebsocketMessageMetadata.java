package com.openstreamingtools.MainServer.twitch;


import lombok.Getter;
import lombok.Setter;

/*
 "metadata": {
        "message_id": "befa7b53-d79d-478f-86b9-120f112b044e",
        "message_type": "notification",
        "message_timestamp": "2022-11-16T10:11:12.464757833Z",
        "subscription_type": "channel.follow",
        "subscription_version": "1"
    }
 */
@Getter
@Setter
public class TwitchWebsocketMessageMetadata {
   private String message_id;
   private String message_type;
   private String message_timestamp;
   private String subscription_type;
   private String subscription_version;
}
