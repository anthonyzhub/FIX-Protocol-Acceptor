package com.demo.quickfix.acceptor.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.bootstrap-server.address}")
    private String bootstrapServer;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        // Connect Kafka dependency to Kafka server
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        return new KafkaAdmin(configMap);
    }

    @Bean
    public NewTopic fixOrdersTopic() {
        // NOTE: Kafka automatically creates topics when a bean returns NewTopic is initialized
        return new NewTopic("FIX-Orders", 1, (short) 1);
    }
}
