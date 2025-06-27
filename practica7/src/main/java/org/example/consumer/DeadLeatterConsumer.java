package org.example.consumer;

import org.example.model.Transaction;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLeatterConsumer {
    @RabbitListener(queues = "dlx.queue")
    public void handleDeadLetter(Object msg){
        System.out.println("messge recived by DLX: " + msg);
    }
}
