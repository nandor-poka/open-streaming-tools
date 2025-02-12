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

    private String name;

    MixerState(String name) {
        this.name = name;
    }


   public static MixerState getByName(String name){
        for (MixerState ms: values()){
            if(ms.name.equals(name)){
                return ms;
            }
        }
        return null;
    }

    @Override
    public String getStateName() {
        return this.name;
    }
}
