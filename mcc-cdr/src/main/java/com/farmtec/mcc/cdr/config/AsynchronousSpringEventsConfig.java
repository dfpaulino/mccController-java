package com.farmtec.mcc.cdr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ComponentScan(basePackages = "com.farmtec.mcc.cdr")
public class AsynchronousSpringEventsConfig {

    @Value("${cdr.pool.max.threads:4}")
    int POOL_MAX_SIZE;

    @Value("${cdr.pool.core.threads:2}")
    int POOL_CORE_SIZE;

    @Value("${cdr.pool.queue.size:2000}")
    int POOL_QUEUE_SIZE;

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster
                = new SimpleApplicationEventMulticaster();

        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(POOL_CORE_SIZE);
        exec.setMaxPoolSize(POOL_MAX_SIZE);
        exec.setQueueCapacity(POOL_QUEUE_SIZE);
        exec.setThreadNamePrefix("cdr-pool-");
        exec.initialize();

        eventMulticaster.setTaskExecutor(exec);
        return eventMulticaster;
    }

}
