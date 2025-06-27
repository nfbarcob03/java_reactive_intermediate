package org.example.consumer;

import org.example.model.Transaction;
import org.example.model.TransactionStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class Consumer {

    @RabbitListener(queues = "trasnaction.created.queue")
    public Transaction handle(Transaction tx){
        tx.setStatus(TransactionStatus.POSTED);
        tx.setCreatedAt(Instant.now());
        return tx;
    }
}
