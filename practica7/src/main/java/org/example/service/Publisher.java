package org.example.service;

import org.example.dto.TransactionDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import static org.example.config.AMQPconfig.EXCHANGE;
import static org.example.config.AMQPconfig.ROUTING_KEY;

@Component
public class Publisher {

    private final AmqpTemplate amqpTemplate;

    public Publisher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }
    public void publish(TransactionDto tx) {
        amqpTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, tx);
    }
}
