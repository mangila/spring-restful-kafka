package com.github.mangila.consumer.kafka;

import com.github.mangila.common.Employee;
import com.github.mangila.common.KafkaHeaders;
import com.github.mangila.common.KafkaTopics;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class EmployeeListenerTest {

    @Container
    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
    @Autowired
    private KafkaAdmin admin;
    private KafkaProducer<String, Employee> producer;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("spring.kafka.consumer.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }

    @BeforeEach
    void setUp() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        this.producer = new KafkaProducer<>(props);
    }

    @Test
    void hasEmployeeTopic() throws ExecutionException, InterruptedException {
        try (var client = AdminClient.create(admin.getConfigurationProperties())) {
            var topicListings = client.listTopics().listings().get();
            assertThat(topicListings.isEmpty()).isFalse();
            assertThat(topicListings.size()).isEqualTo(1);
            var hasEmployeeTopic = topicListings.stream()
                    .map(TopicListing::name)
                    .anyMatch(name -> name.equals(KafkaTopics.EMPLOYEE_TOPIC));
            assertThat(hasEmployeeTopic).isTrue();
        }
    }

    @Test
    void listen(CapturedOutput output) throws InterruptedException {
        var now = Instant.now();
        var e = Employee.builder()
                .id("1")
                .name("John")
                .updated(now)
                .build();
        var record = new ProducerRecord<>(KafkaTopics.EMPLOYEE_TOPIC, e.getId(), e);
        record.headers().add(KafkaHeaders.CONSUMER_OPERATION, "insert".getBytes());
        producer.send(record);
        Thread.sleep(1000);
        assertThat(output).contains("EmployeeService.insert " + e + " was inserted!");
    }
}