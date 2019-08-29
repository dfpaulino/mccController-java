package com.farmtec.amqp.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor
public class MccEventAmqpDto implements Serializable {
    String address;
    String module;
    String moduleId;
    int value;

    @Override
    public String toString(){
        return "{ \"address\":"+address+",\n\"module\":"+module+",\n \"moduleId\":"+moduleId+",\n \"value\":"+value+"}";
    }

}
