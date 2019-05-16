package com.farmtec.mcc.cdr.logger;

import com.farmtec.mcc.cdr.dto.Cdr;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
@Component
@Profile("cdr-logback")
public class CdrLoggerLogback implements CdrLogger{

    Logger logger = LoggerFactory.getLogger(CdrLoggerLogback.class);
    @Override
    public void log(Cdr cdr) {
        if(null==cdr.getNow()){
            cdr.setNow(new Date());
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy,hh:mm:ss");
        String cdrStr=cdr.getAddr()+","+cdr.getOperation()+","+simpleDateFormat.format(cdr.getNow())+","+cdr.getData();
        logger.info(cdrStr);

    }
}
