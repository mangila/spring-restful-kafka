package com.github.mangila.consumer.config;

import com.github.mangila.common.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class MapConfig {

    @Bean
    public ConcurrentHashMap<String, Employee> map() {
        return new ConcurrentHashMap<>();
    }

}
