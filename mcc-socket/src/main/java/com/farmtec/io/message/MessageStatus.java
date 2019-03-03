package com.farmtec.io.message;

public enum MessageStatus {
    INCOMPLETE(0),COMPLETE(1),COMPLETE_EXCEDED(2);

    int value;

    MessageStatus(int value) {
        this.value = value;
    }
    int getValue(){return this.value;}
}
