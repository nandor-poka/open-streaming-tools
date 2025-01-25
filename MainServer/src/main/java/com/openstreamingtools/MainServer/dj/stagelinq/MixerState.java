package com.openstreamingtools.MainServer.dj.stagelinq;

public enum MixerState implements State{
    MixerCH1faderPosition("/Mixer/CH1faderPosition"),
    MixerCH2faderPosition("/Mixer/CH2faderPosition"),
    MixerCH3faderPosition("/Mixer/CH3faderPosition"),
    MixerCH4faderPosition("/Mixer/CH4faderPosition"),
    MixerCrossfaderPosition("/Mixer/CrossfaderPosition"),
    MixerChannelAssignment1("/Mixer/ChannelAssignment1"),
    MixerChannelAssignment2("/Mixer/ChannelAssignment2"),
    MixerChannelAssignment3("/Mixer/ChannelAssignment3"),
    MixerChannelAssignment4("/Mixer/ChannelAssignment4"),
    MixerNumberOfChannels("/Mixer/NumberOfChannels");

    private String value;

    MixerState(String value) {
        this.value = value;
    }

    String getValue(){
        return this.value;
    }

    static MixerState getByValue(String value){
        for (MixerState ms: values()){
            if(ms.value.equals(value)){
                return ms;
            }
        }
        return null;
    }

    @Override
    public String getStateValue() {
        return this.value;
    }
}
