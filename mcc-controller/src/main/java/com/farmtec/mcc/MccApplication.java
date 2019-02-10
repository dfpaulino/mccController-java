package com.farmtec.mcc;

import com.farmtec.mcc.repositories.config.RepoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication()
@Import(RepoConfig.class)
public class MccApplication {

	public static void main(String[] args) {
		SpringApplication.run(MccApplication.class, args);
	}
}
