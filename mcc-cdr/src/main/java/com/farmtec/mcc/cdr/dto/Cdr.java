package com.farmtec.mcc.cdr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter @Setter
public class Cdr {


    private String addr;
    private int operation;
private Date  now;
private String data;

}
