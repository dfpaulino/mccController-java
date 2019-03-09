package com.farmtec.mcc.utils;

public class Util {
    public static String IntegerToByteReadableHex(int value){ return "0x"+Integer.toHexString(value & 0xFF);}

}
