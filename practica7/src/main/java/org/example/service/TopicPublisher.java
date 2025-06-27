package org.example.service;

import org.example.dto.TransactionDto;
import org.example.model.TransactionType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static org.example.config.AMQPconfig.TOPIC_EXCHANGE;

@Component
public class TopicPublisher {
    private final RabbitTemplate rabbitTemplate;

    public TopicPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public Mono<Void> publishTransaction(TransactionDto transactionDto) {
        String routingKey = transactionDto.type()== TransactionType.CASH_IN?"cash.in":"cash.out";
        return Mono.fromRunnable(() -> {
            rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, routingKey, transactionDto);
        }).subscribeOn(Schedulers.boundedElastic())
          .then();
    }
}
