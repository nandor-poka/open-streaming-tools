package com.openstreamingtools.backend.messages.stagelinqmessages;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter

public class StateMapMessage<T> {

    private int header;
    private T data ;

    public StateMapMessage() {
    }

    @Override
    public String toString() {
        return "StateMapMessage{" +
                "header=" + header +
                ", data=" + data +
                '}';
    }
}
