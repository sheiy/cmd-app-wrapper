package com.github.sheiy.cmdappwrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class Beans {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean(destroyMethod = "shutdown")
    ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(3, 10, 1L, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                final Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("customer thread pool");
                return thread;
            }
        });
    }
}
