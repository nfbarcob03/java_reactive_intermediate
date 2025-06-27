package org.example.consumer;

import org.example.model.Transaction;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CashInConsumer {
    @RabbitListener(queues = "cash.in.queue")
    public void handleCashIn(Transaction tx){
        System.out.println("Received CASH IN transaction: " + tx);
    }
}
