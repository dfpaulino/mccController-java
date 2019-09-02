package com.farmtec.amqp.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter @NoArgsConstructor
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

    public MccEventAmqpDto setMccAddress(String address){this.mccAddress=address; return this;}
    public MccEventAmqpDto setModule(String module){this.module=module; return this;}
    public MccEventAmqpDto setModuleId(String moduleId){this.moduleId=moduleId; return this;}
    public MccEventAmqpDto setId(int id){this.id=id; return this;}
    public MccEventAmqpDto setValue(int value){this.value=value; return this;}

}
