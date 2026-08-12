package com.openstreamingtools.MainServer.messages;

public enum MessageType {
    GENERAL_MESSAGE("General Message"),
    SONG_DATA("SongData"),
    CHANNEL_VOLUME_DATA("ChannelVolumeData"),
    STAGELINQ_DISCOVERY_MESSAGE("StageLinQ Discovery Message"),
    UNIT_DATA("Unit data"),
    ACKNOWLEDMENT_MESSAGE("Acknowledgement message");
    private final String name;

    MessageType(String type) {
        this.name = type;
    }

    public String getName() {
        return name;
    }

    public static MessageType getByName(String name) {
        for (MessageType type : MessageType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
