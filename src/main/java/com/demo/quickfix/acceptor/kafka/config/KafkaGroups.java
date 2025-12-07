package com.demo.quickfix.acceptor.kafka.config;

import lombok.Getter;

@Getter
public enum KafkaGroups {
    ORDER_ID(1);

    int numberId;
    KafkaGroups(int numberId) {
        this.numberId = numberId;
    }
}
