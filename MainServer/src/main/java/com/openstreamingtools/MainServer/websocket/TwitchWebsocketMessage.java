package com.openstreamingtools.MainServer.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchWebsocketMessage {
    TwitchWebsocketMessageMetadata metadata;
    TwithcWebsocketMessagePayload payload;
}
