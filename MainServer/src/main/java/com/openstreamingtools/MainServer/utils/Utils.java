package com.openstreamingtools.MainServer.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Utils {

    public static final int DIRECTORY_SERVICE_PORT = 60000;
    public static final int STATEMAP_SERVICE_PORT = 60001;

    public static void putIntegerToByteArray(int i, byte[] array){
        array[0] = (byte)((i >> 24)& 0xFF);
        array[1] = (byte)((i >> 16)& 0xFF);
        array[2] = (byte)((i >> 8)& 0xFF);
        array[3] = (byte)(i & 0xFF);
    }

    public static byte[] convertIntegerToByteArray(int i){
        byte[] bytes = new byte[4];
        bytes[0] = (byte)((i >> 24)& 0xFF);
        bytes[1] = (byte)((i >> 16)& 0xFF);
        bytes[2] = (byte)((i >> 8)& 0xFF);
        bytes[3] = (byte)(i & 0xFF);
        return bytes;
    }
    public static byte[] convertShortToByteArray(int i){
        byte[] bytes = new byte[2];
        bytes[0] = (byte)((i >> 8)& 0xFF);
        bytes[1] = (byte)(i & 0xFF);
        return bytes;
    }

    public static byte[] convertUUIDToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID convertBytesToUUID(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();
        return new UUID(high, low);
    }

    public static byte[] getNetworkBytesWithLenghForString(String string, Charset charset){
        byte[] stringAsBytes = string.getBytes(charset);
        byte[] intAsBytes = convertIntegerToByteArray(stringAsBytes.length);
        byte[] networkBytes = new byte[4+stringAsBytes.length];

        // add length first
        for (int i=0;i<intAsBytes.length;i++){
            networkBytes[i] = intAsBytes    [i];
        }

        for (int i = 0; i < stringAsBytes.length; i++){
            networkBytes[i+4] = stringAsBytes[i];
        }
        return networkBytes;
     }

}