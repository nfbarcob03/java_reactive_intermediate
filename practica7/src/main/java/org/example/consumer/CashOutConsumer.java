package org.example.consumer;

import org.example.model.Transaction;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CashOutConsumer {
    @RabbitListener(queues = "cash.out.queue")
    public void handleCashOut(Transaction tx){
        System.out.println("Received CASH OUT transaction: " + tx);
    }
}
