package com.demo.quickfix.acceptor.kafka;

import com.demo.quickfix.acceptor.kafka.config.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import quickfix.fix42.ExecutionReport;

@Slf4j
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, ExecutionReport> kafkaTemplate;

    @KafkaListener(topics = "FIX_ORDERS", groupId = "ORDER_ID")
    public void listenGroupOrders(String message) {
        log.info("Received Message in group [Order]: {}", message);
    }

    public void sendKafkaMessage(ExecutionReport executionReport) {
        kafkaTemplate.send(KafkaTopics.FIX_ORDERS.toString(), executionReport);
    }
}
