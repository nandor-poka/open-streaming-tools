package com.openstreamingtools.MainServer.twitch;

import com.openstreamingtools.MainServer.config.OSTConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class TwitchSubscribtionCondition {
    public String broadcaster_user_id;

    public TwitchSubscribtionCondition(){
        broadcaster_user_id = OSTConfiguration.settings.getTwitchUser().getId();
    }
}

