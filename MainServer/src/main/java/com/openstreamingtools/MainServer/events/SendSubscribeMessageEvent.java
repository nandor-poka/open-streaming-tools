package com.openstreamingtools.MainServer.events;

import com.openstreamingtools.MainServer.messages.stagelinqmessages.StateMapSubscribeMessage;
import org.springframework.context.ApplicationEvent;

public class SendSubscribeMessageEvent extends ApplicationEvent {

    private StateMapSubscribeMessage subscribeMessage;

    public SendSubscribeMessageEvent(Object source) {
        super(source);
    }

    public SendSubscribeMessageEvent(Object source, StateMapSubscribeMessage subscribeMessage) {
        super(source);
        this.subscribeMessage = subscribeMessage;
    }

    public StateMapSubscribeMessage getSubscribeMessage() {
        return subscribeMessage;
    }
}
