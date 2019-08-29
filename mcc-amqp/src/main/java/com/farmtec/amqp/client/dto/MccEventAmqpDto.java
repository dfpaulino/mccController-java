package com.farmtec.amqp.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor
public class MccEventAmqpDto implements Serializable {
    String mccAddress;
    String module;
    String moduleId;
    int id;
    int value;

    @Override
    public String toString(){
        return "{ \"address\":"+mccAddress+",\n\"module\":"+module+",\n \"moduleId\":"+moduleId+",\n \"value\":"+value+",\n \"id\":"+id+"}";
    }

}
