package com.openstreamingtools.MainServer.dj.stagelinq;

public enum ActingAs {
    SOUNDSWITCH(new byte[]{82, (byte) 253, (byte)252, 7, 33, (byte)130, 101, 79, 22, 63, 95, 15, (byte)154, 98, 29, 114}),
    SC6000_1(new byte[]{(byte)130, (byte)139, (byte)235, 2, (byte)218, 31, 78, 104,(byte) 166,(byte) 175,(byte) 176, (byte)177,
            103,(byte) 234,(byte) 240, (byte)162}),
    SS6000_2(new byte[]{38, (byte)210, 56, 103, 28, (byte) 214, 78, 63, (byte)128,(byte) 161, 17, (byte)130, 106, (byte)196, 17, 32}),
    RESOLUME(new byte[]{(byte)136, (byte)250, 32, (byte)153, (byte)172, 122, 79, 63, (byte)188, 22, (byte)169, (byte)149,
            (byte)219, (byte)218, 42, 66}),
    LISTEN(new byte[]{(byte)255, (byte)255, (byte)255, (byte)255, (byte)255, (byte)255,
            74, 28, (byte)155, (byte)186, (byte)136, (byte)180, (byte)190, 25, (byte)163, (byte)209});

    private final byte[] value;
    ActingAs(byte[] bytes) {
        this.value = bytes;
    }

    public byte[] getValue(){
        return value;
    }


}
