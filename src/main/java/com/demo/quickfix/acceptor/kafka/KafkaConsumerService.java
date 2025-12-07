package com.demo.quickfix.acceptor.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    @KafkaListener(topics = "FIX-Orders", groupId = "orders")
    public void listenGroupOrders(String message) {
        log.info("Received Message in group [Order]: {}", message);
    }
}
