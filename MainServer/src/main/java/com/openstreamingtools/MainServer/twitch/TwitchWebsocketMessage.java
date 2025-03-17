package com.openstreamingtools.MainServer.twitch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TwitchWebsocketMessage {
    TwitchWebsocketMessageMetadata metadata;
    TwithcWebsocketMessagePayload payload;
}
