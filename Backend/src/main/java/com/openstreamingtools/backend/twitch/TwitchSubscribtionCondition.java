package com.openstreamingtools.backend.twitch;

import com.openstreamingtools.backend.config.OSTConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TwitchSubscribtionCondition {
    public String broadcaster_user_id;

    public TwitchSubscribtionCondition(){
        broadcaster_user_id = OSTConfiguration.settings.getTwitchUser().getId();
    }
}

