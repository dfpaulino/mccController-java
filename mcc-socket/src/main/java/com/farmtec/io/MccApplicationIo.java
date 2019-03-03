package com.farmtec.io;

import com.farmtec.io.config.EnableIO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication(scanBasePackages = "com.farmtec.io")
@EnableIO
public class MccApplicationIo {

	public static void main(String[] args) {
		SpringApplication.run(MccApplicationIo.class, args);
	}
}
