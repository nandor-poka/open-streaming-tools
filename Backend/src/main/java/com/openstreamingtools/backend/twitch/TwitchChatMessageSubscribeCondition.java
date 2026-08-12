package com.openstreamingtools.backend.twitch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
public class TwitchChatMessageSubscribeCondition extends TwitchSubscribtionCondition {
    private String user_id;

}

