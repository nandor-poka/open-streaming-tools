package com.openstreamingtools.MainServer.twitch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TwitchSubscribeCondition {
    private String broadcaster_user_id;
    private String user_id;
}
