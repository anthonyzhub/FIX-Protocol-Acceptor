package com.demo.quickfix.acceptor.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import quickfix.fix42.ExecutionReport;

@Slf4j
@RequiredArgsConstructor
public class KafkaService {

    private static final String FIX_ORDER_TOPIC = "FIX-Orders";
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "FIX-Orders", groupId = "orders")
    public void listenGroupOrders(String message) {
        log.info("Received Message in group [Order]: {}", message);
    }

    public void sendKafkaMessage(ExecutionReport executionReport) {
        kafkaTemplate.send(executionReport.toString(), FIX_ORDER_TOPIC);
    }
}
