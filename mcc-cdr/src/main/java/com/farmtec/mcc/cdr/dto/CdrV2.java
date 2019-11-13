package com.farmtec.mcc.cdr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


final class CdrV2 {


    private String addr;
    private int operation;
    private Date  now;
    private String data;

    public CdrV2(Builder builder){
        this.addr=builder.addr;
        this.operation=builder.operation;
        this.now=builder.now;
        this.data=builder.data;
    }

    public static  class  Builder{
        private String addr;
        private int operation;
        private Date  now;
        private String data;

        public static Builder getInstance(){return new Builder();}

        public Builder setAddr(String addr) {
            this.addr = addr;
            return this;
        }

        public Builder setOperation(int operation) {
            this.operation = operation;
            return this;
        }

        public Builder setNow(Date now) {
            this.now = now;
            return this;
        }

        public Builder setData(String data) {
            this.data = data;
            return this;
        }
        public CdrV2 build(){
            return new CdrV2(this);
        }
    }

}
