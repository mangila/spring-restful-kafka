package com.github.mangila.producer.kafka;

import com.github.mangila.common.Employee;
import com.github.mangila.common.KafkaTopics;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class EmployeeProducerTest {
    @Container
    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
    @Autowired
    private EmployeeProducer producer;
    @Autowired
    private KafkaAdmin admin;
    private KafkaConsumer<String, Employee> consumer;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }

    @BeforeEach
    void beforeEach() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "employee-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        this.consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of(KafkaTopics.EMPLOYEE_TOPIC));
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
    void produce(CapturedOutput output) throws InterruptedException {
        var now = Instant.now();
        var e = Employee.builder()
                .id("1")
                .name("John")
                .updated(now)
                .build();
        producer.insert(e);
        Thread.sleep(1000);
        assertThat(output).contains("EmployeeService.insert - message success!");
        await().atMost(Duration.ofSeconds(10)).until(() -> {
            var record = consumer.poll(Duration.ofMillis(100));
            if (record.isEmpty()) {
                return false;
            }
            assertThat(record.count()).isEqualTo(1);
            var employee = record.iterator().next().value();
            assertThat(employee).extracting(Employee::getId,
                            Employee::getName,
                            Employee::getUpdated)
                    .doesNotContainNull()
                    .containsExactly("1", "John", now);
            return true;
        });
    }
}