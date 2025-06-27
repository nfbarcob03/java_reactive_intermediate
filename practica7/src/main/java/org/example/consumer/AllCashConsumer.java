package org.example.consumer;

import org.example.model.Transaction;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AllCashConsumer {

    @RabbitListener(queues = "cash.all.queue")
    public void handleAllCash(Transaction tx){
        System.out.println("[ALL CASH] Received transaction: " + tx);
    }
}
