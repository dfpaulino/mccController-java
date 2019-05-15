package com.farmtec.mcc.cdr.dto;

import lombok.Getter;

@Getter
public enum CdrType {
    UPDATE_INFO_RESPONSE(1),UPDATE_INFO_REQUEST(2),UPDATE_REQUEST(3);

    CdrType(int value) {
        this.value = value;
    }

    private int value;

}
