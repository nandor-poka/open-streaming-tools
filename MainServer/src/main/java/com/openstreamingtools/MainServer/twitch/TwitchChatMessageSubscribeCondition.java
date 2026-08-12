package com.openstreamingtools.MainServer.twitch;

import lombok.*;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
public class TwitchChatMessageSubscribeCondition extends TwitchSubscribtionCondition {
    private String user_id;

}

