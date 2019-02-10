package com.farmtec.mcc.repositories.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableJpaRepositories(basePackages = {"com.farmtec.mcc.repositories"})
@EntityScan(basePackages = "com.farmtec.mcc.models")
@EnableJpaAuditing
@EnableTransactionManagement
public class RepoConfig {
}
