package com.github.mangila.producer.config;

import com.github.mangila.common.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic employee() {
        return TopicBuilder.name(KafkaTopics.EMPLOYEE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
