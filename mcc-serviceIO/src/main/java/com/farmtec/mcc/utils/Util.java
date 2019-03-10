package com.farmtec.mcc.utils;

public class Util {
    public static String IntegerToByteReadableHex(int value) {
        String hexStr = "0x00";
        if (value <= 0xf) {
            hexStr = "0x0" + Integer.toHexString(value & 0xF);
        } else {
            hexStr = "0x" + Integer.toHexString(value & 0xFF);
        }
        return hexStr;
    }
}
